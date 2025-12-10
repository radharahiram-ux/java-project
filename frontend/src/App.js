import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import { LockClosedIcon, TrendingUpIcon, ExclamationTriangleIcon, ShoppingCartIcon, HomeIcon, ChartBarIcon, LogoutIcon } from '@heroicons/react/24/solid';
import * as tf from '@tensorflow/tfjs';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

// Login Component
function Login({ setUser }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    if (email && password) {
      setUser({ email });
      navigate('/dashboard');
    }
    setLoading(false);
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md animate-fade-in">
        <h2 className="text-2xl font-bold text-center mb-6 text-gray-800">Welcome to StockTrader</h2>
        <form onSubmit={handleSubmit}>
          <input 
            type="email" 
            placeholder="Email" 
            value={email} 
            onChange={(e) => setEmail(e.target.value)} 
            className="w-full p-3 mb-4 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500" 
            required 
          />
          <input 
            type="password" 
            placeholder="Password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
            className="w-full p-3 mb-4 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500" 
            required 
          />
          <button 
            type="submit" 
            className="w-full bg-green-500 text-white p-3 rounded-lg hover:bg-green-600 transition duration-200 flex items-center justify-center" 
            disabled={loading}
          >
            {loading ? <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div> : <><LockClosedIcon className="h-5 w-5 mr-2" /> Login</>}
          </button>
        </form>
        <p className="text-center mt-4 text-sm text-gray-600">Demo: Any email/password works.</p>
      </div>
    </div>
  );
}

// Dashboard Component
function Dashboard({ user, stocks, model }) {
  const [predictions, setPredictions] = useState({});
  const [chartData, setChartData] = useState({});

  useEffect(() => {
    setChartData({
      labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
      datasets: [{
        label: 'Portfolio Value',
        data: [10000, 10500, 10200, 10800, 11000],
        borderColor: 'rgb(34, 197, 94)',
        backgroundColor: 'rgba(34, 197, 94, 0.2)',
      }],
    });
  }, []);

  useEffect(() => {
    if (!model) return;
    const predictPrices = async () => {
      const preds = {};
      for (const [symbol, data] of Object.entries(stocks)) {
        const currentPrice = parseFloat(data['05. price']) || 0;
        if (!currentPrice) {
          preds[symbol] = 0;
          continue;
        }
        try {
          const histResponse = await fetch(`https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=${symbol}&apikey=${process.env.REACT_APP_ALPHA_VANTAGE_KEY}&outputsize=compact`);
          const histData = await histResponse.json();
          const timeSeries = histData['Time Series (Daily)'] || {};
          const prices = Object.values(timeSeries).slice(0, 30).map(d => parseFloat(d['4. close'])).filter(p => !isNaN(p));
          if (prices.length === 0) {
            preds[symbol] = currentPrice * 1.05;
            continue;
          }
          const input = tf.tensor2d([prices.slice(0, 1)], [1, 1]);
          const pred = model.predict(input);
          const predictedPrice = pred.dataSync()[0] * (currentPrice / (prices[0] || currentPrice));
          preds[symbol] = predictedPrice;
          input.dispose();
          pred.dispose();
        } catch (err) {
          console.error(`Prediction failed for ${symbol}:`, err);
          preds[symbol] = currentPrice * 1.05;
        }
      }
      setPredictions(preds);
    };
    predictPrices();
  }, [stocks, model]);

  const getPredictionColor = (current, predicted) => predicted > current ? 'text-green-500' : 'text-red-500';

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6 text-gray-800 flex items-center"><TrendingUpIcon className="h-8 w-8 mr-2 text-green-500" /> Smart Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {Object.entries(stocks).map(([symbol, data]) => (
          <div key={symbol} className="bg-white p-4 rounded-lg shadow-md hover:shadow-lg transition duration-200 animate-fade-in">
            <h3 className="text-xl font-semibold">{symbol}</h3>
            <p className="text-gray-600">Current: ${data['05. price']}</p>
            <p className={`font-semibold ${predictions[symbol] ? getPredictionColor(parseFloat(data['05. price']), predictions[symbol]) : 'text-gray-500'}`}>
              AI Predicted: ${predictions[symbol]?.toFixed(2) || 'Calculating...'}
              {predictions[symbol] && <ExclamationTriangleIcon className="h-5 w-5 inline ml-1" />}
            </p>
            <p className="text-sm text-gray-500">Trend: {predictions[symbol] && predictions[symbol] > parseFloat(data['05. price']) ? 'Bullish' : 'Bearish'}</p>
          </div>
        ))}
      </div>
      <div className="mt-8 bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-4">Portfolio Performance with AI Insights</h2>
        <Line data={chartData} />
      </div>
    </div>
  );
}

// Portfolio Component
function Portfolio({ user, portfolio, balance, model }) {
  const [predictions, setPredictions] = useState({});

  useEffect(() => {
    if (!model || portfolio.length === 0) return;
    const predictPortfolio = async () => {
      const preds = {};
      for (const stock of portfolio) {
        try {
          const input = tf.tensor2d([[stock.avgPrice]], [1, 1]);
          const pred = model.predict(input);
          preds[stock.stockSymbol] = pred.dataSync()[0];
          input.dispose();
          pred.dispose();
        } catch (err) {
          console.error(`Prediction failed for ${stock.stockSymbol}:`, err);
          preds[stock.stockSymbol] = stock.avgPrice * 1.05;
        }
      }
      setPredictions(preds);
    };
    predictPortfolio();
  }, [portfolio, model]);

  const totalValue = portfolio.reduce((sum, stock) => sum + (stock.quantity * stock.avgPrice), 0);

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6 text-gray-800">Your AI-Enhanced Portfolio</h1>
      <p className="text-lg mb-4">Balance: <span className="font-semibold text-green-600">${balance.toFixed(2)}</span></p>
      <p className="text-lg mb-6">Portfolio Value: <span className="font-semibold text-blue-600">${totalValue.toFixed(2)}</span></p>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {portfolio.length > 0 ? (
          portfolio.map((stock, idx) => (
            <div key={idx} className="bg-white p-4 rounded-lg shadow-md hover:scale-105 transition duration-200">
              <h3 className="text-xl font-semibold">{stock.stockSymbol}</h3>
              <p>Quantity: {stock.quantity}</p>
              <p>Avg Price: ${stock.avgPrice.toFixed(2)}</p>
              <p className="text-blue-500 font-semibold">AI Prediction: ${predictions[stock.stockSymbol]?.toFixed(2) || 'Loading...'}</p>
              <p className="text-gray-600">Total Value: ${(stock.quantity * stock.avgPrice).toFixed(2)}</p>
            </div>
          ))
        ) : (
          <p className="text-gray-500">No stocks in portfolio. Go to Buy/Sell to add stocks.</p>
        )}
      </div>
    </div>
  );
}

// BuySell Component
function BuySell({ user, portfolio, setPortfolio, balance, setBalance }) {
  const [symbol, setSymbol] = useState('');
  const [quantity, setQuantity] = useState('1');
  const [type, setType] = useState('buy');

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!symbol.trim() || !quantity || parseInt(quantity) <= 0) {
      alert('Please enter valid symbol and quantity');
      return;
    }

    const price = 150; // Mock price
    const qty = parseInt(quantity);
    const cost = price * qty;

    if (type === 'buy') {
      if (balance >= cost) {
        setBalance(balance - cost);
        const existing = portfolio.find(p => p.stockSymbol === symbol.toUpperCase());
        if (existing) {
          existing.quantity += qty;
          setPortfolio([...portfolio]);
        } else {
          setPortfolio([...portfolio, { stockSymbol: symbol.toUpperCase(), quantity: qty, avgPrice: price }]);
        }
        alert(`Bought ${qty} shares of ${symbol.toUpperCase()} successfully!`);
      } else {
        alert('Insufficient balance!');
      }
    } else if (type === 'sell') {
      const existing = portfolio.find(p => p.stockSymbol === symbol.toUpperCase());
      if (existing && existing.quantity >= qty) {
        existing.quantity -= qty;
        if (existing.quantity === 0) {
          setPortfolio(portfolio.filter(p => p.stockSymbol !== symbol.toUpperCase()));
        } else {
          setPortfolio([...portfolio]);
        }
        setBalance(balance + cost);
        alert(`Sold ${qty} shares of ${symbol.toUpperCase()} successfully!`);
      } else {
        alert('Insufficient shares!');
      }
    }

    setSymbol('');
    setQuantity('1');
  };

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6 text-gray-800 flex items-center"><ShoppingCartIcon className="h-8 w-8 mr-2 text-blue-500" /> Buy/Sell Stocks</h1>
      <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md max-w-md mx-auto">
        <select 
          value={type} 
          onChange={(e) => setType(e.target.value)} 
          className="w-full p-3 mb-4 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="buy">Buy</option>
          <option value="sell">Sell</option>
        </select>
        <input 
          type="text" 
          placeholder="Stock Symbol (e.g., AAPL)" 
          value={symbol} 
          onChange={(e) => setSymbol(e.target.value)} 
          className="w-full p-3 mb-4 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" 
          required 
        />
        <input 
          type="number" 
          placeholder="Quantity" 
          value={quantity} 
          onChange={(e) => setQuantity(e.target.value)} 
          className="w-full p-3 mb-4 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" 
          min="1" 
          required 
        />
        <p className="text-gray-600 mb-4">Total Cost: ${(150 * (parseInt(quantity) || 0)).toFixed(2)}</p>
        <button 
          type="submit" 
          className="w-full bg-blue-500 text-white p-3 rounded-lg hover:bg-blue-600 transition duration-200 font-semibold"
        >
          {type === 'buy' ? 'Buy Stock' : 'Sell Stock'}
        </button>
      </form>
      <div className="mt-8 bg-blue-50 p-4 rounded-lg max-w-md mx-auto">
        <p className="text-sm text-gray-600"><strong>Current Balance:</strong> ${balance.toFixed(2)}</p>
        <p className="text-sm text-gray-600"><strong>Mock Stock Price:</strong> $150.00</p>
      </div>
    </div>
  );
}

// Navbar Component
function Navbar({ user, setUser }) {
  const navigate = useNavigate();

  const logout = () => {
    setUser(null);
    navigate('/');
  };

  return (
    <nav className="bg-white shadow-md p-4">
      <div className="container mx-auto flex justify-between items-center">
        <h1 className="text-xl font-bold text-gray-800">StockTrader</h1>
        <div className="flex space-x-4 items-center">
          <a href="/dashboard" className="flex items-center text-gray-600 hover:text-blue-500 transition"><HomeIcon className="h-5 w-5 mr-1" /> Dashboard</a>
          <a href="/portfolio" className="flex items-center text-gray-600 hover:text-blue-500 transition"><ChartBarIcon className="h-5 w-5 mr-1" /> Portfolio</a>
          <a href="/buy-sell" className="flex items-center text-gray-600 hover:text-blue-500 transition"><ShoppingCartIcon className="h-5 w-5 mr-1" /> Buy/Sell</a>
          <button onClick={logout} className="flex items-center text-gray-600 hover:text-red-500 transition"><LogoutIcon className="h-5 w-5 mr-1" /> Logout</button>
        </div>
      </div>
    </nav>
  );
}

// Main App Component
function App() {
  const [user, setUser] = useState(null);
  const [stocks, setStocks] = useState({});
  const [model, setModel] = useState(null);
  const [portfolio, setPortfolio] = useState([{ stockSymbol: 'AAPL', quantity: 10, avgPrice: 150 }]);
  const [balance, setBalance] = useState(10000);

  useEffect(() => {
    const loadModel = async () => {
      try {
        const loadedModel = await tf.loadLayersModel('/tfjs_model/model.json');
        setModel(loadedModel);
      } catch (err) {
        console.error('Model loading failed (using mock):', err);
        setModel(true); // Use as flag for mock predictions
      }
    };
    loadModel();
  }, []);

  useEffect(() => {
    const fetchStocks = async () => {
      const symbols = ['AAPL', 'GOOGL', 'TSLA'];
      const data = {};
      for (const sym of symbols) {
        try {
          const response = await fetch(`https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=${sym}&apikey=${process.env.REACT_APP_ALPHA_VANTAGE_KEY}`);
          const result = await response.json();
          data[sym] = result['Global Quote'] || { '05. price': Math.random() * 300 + 50 };
        } catch (err) {
          console.error(`Failed to fetch ${sym}:`, err);
          data[sym] = { '05. price': Math.random() * 300 + 50 };
        }
      }
      setStocks(data);
    };
    fetchStocks();
  }, []);

  return (
    <Router>
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-green-50">
        {user && <Navbar user={user} setUser={setUser} />}
        <Routes>
          <Route path="/" element={<Login setUser={setUser} />} />
          <Route path="/dashboard" element={user ? <Dashboard user={user} stocks={stocks} model={model} /> : <Login setUser={setUser} />} />
          <Route path="/portfolio" element={user ? <Portfolio user={user} portfolio={portfolio} balance={balance} model={model} /> : <Login setUser={setUser} />} />
          <Route path="/buy-sell" element={user ? <BuySell user={user} portfolio={portfolio} setPortfolio={setPortfolio} balance={balance} setBalance={setBalance} /> : <Login setUser={setUser} />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
