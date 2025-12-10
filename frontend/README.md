# StockTrader - AI-Enhanced Stock Trading Platform

## Setup Instructions

### Prerequisites
- Node.js (v14 or higher)
- npm or yarn

### Installation

1. **Install dependencies:**
   ```bash
   npm install
   ```

2. **Set up environment variables:**
   Create a `.env` file in the root directory:
   ```
   REACT_APP_ALPHA_VANTAGE_KEY=your_api_key_here
   ```
   Get your free API key from: https://www.alphavantage.co/

3. **Start the development server:**
   ```bash
   npm start
   ```

The app will run on `http://localhost:3000`

## Features Fixed

✅ **Fixed Issues:**
- TensorFlow tensor shape errors (corrected 3D to 2D tensors)
- Null/undefined prediction handling
- Proper TensorFlow memory management (dispose tensors)
- Input validation for buy/sell operations
- Safe array access with fallback values
- Improved error handling throughout

✅ **Includes:**
- Login component (demo mode - any email/password)
- AI-powered price predictions
- Portfolio management
- Buy/Sell functionality
- Real-time stock data from Alpha Vantage API
- TensorFlow.js integration for ML predictions
- Responsive Tailwind CSS design

## Demo Credentials
- Email: any@email.com
- Password: any

## Project Structure
```
frontend/
├── src/
│   ├── App.js (main component)
│   ├── index.js (entry point)
│   └── index.css (styles)
├── public/
│   └── index.html
├── package.json
└── tsconfig.json
```
