// Main.java
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static PlacementSystem system = new PlacementSystem();
    private static User currentUser = null;

    public static void main(String[] args) {
        // Add sample data
        initializeSampleData();
        
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  Campus Placement & Career Services System    ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                if (currentUser instanceof Student) {
                    showStudentMenu();
                } else if (currentUser instanceof Company) {
                    showCompanyMenu();
                } else if (currentUser instanceof Admin) {
                    showAdminMenu();
                }
            }
        }
    }

    private static void initializeSampleData() {
        // Add sample students
        Student s1 = new Student("S001", "Alice Johnson", "alice@college.edu", "CS", 8.5, 2024);
        s1.addSkill("Java");
        s1.addSkill("Python");
        s1.addSkill("SQL");
        system.addStudent(s1);

        Student s2 = new Student("S002", "Bob Smith", "bob@college.edu", "IT", 7.8, 2024);
        s2.addSkill("JavaScript");
        s2.addSkill("React");
        s2.addSkill("Node.js");
        system.addStudent(s2);

        // Add sample companies
        Company c1 = new Company("C001", "TechCorp", "hr@techcorp.com", "Technology");
        system.addCompany(c1);

        Company c2 = new Company("C002", "DataSystems Inc", "recruit@datasystems.com", "IT Services");
        system.addCompany(c2);

        // Add admin
        system.addAdmin(new Admin("A001", "Admin User", "admin@college.edu"));
    }

    private static void showLoginMenu() {
        System.out.println("\n=== Login Menu ===");
        System.out.println("1. Student Login");
        System.out.println("2. Company Login");
        System.out.println("3. Admin Login");
        System.out.println("4. Exit");
        System.out.print("Choose option: ");
        
        int choice = getIntInput();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                studentLogin();
                break;
            case 2:
                companyLogin();
                break;
            case 3:
                adminLogin();
                break;
            case 4:
                System.out.println("Thank you for using the system!");
                System.exit(0);
            default:
                System.out.println("Invalid option!");
        }
    }

    private static void studentLogin() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        Student student = system.getStudentById(id);
        if (student != null) {
            currentUser = student;
            System.out.println("Welcome, " + student.getName() + "!");
        } else {
            System.out.println("Student not found!");
        }
    }

    private static void companyLogin() {
        System.out.print("Enter Company ID: ");
        String id = scanner.nextLine();
        Company company = system.getCompanyById(id);
        if (company != null) {
            currentUser = company;
            System.out.println("Welcome, " + company.getName() + "!");
        } else {
            System.out.println("Company not found!");
        }
    }

    private static void adminLogin() {
        System.out.print("Enter Admin ID: ");
        String id = scanner.nextLine();
        Admin admin = system.getAdminById(id);
        if (admin != null) {
            currentUser = admin;
            System.out.println("Welcome, " + admin.getName() + "!");
        } else {
            System.out.println("Admin not found!");
        }
    }

    private static void showStudentMenu() {
        Student student = (Student) currentUser;
        System.out.println("\n=== Student Menu ===");
        System.out.println("1. View Profile");
        System.out.println("2. Update Profile");
        System.out.println("3. View Available Jobs");
        System.out.println("4. Apply for Job");
        System.out.println("5. View My Applications");
        System.out.println("6. View Interview Schedule");
        System.out.println("7. View Offers");
        System.out.println("8. Skill Gap Analysis");
        System.out.println("9. Logout");
        System.out.print("Choose option: ");
        
        int choice = getIntInput();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                viewStudentProfile(student);
                break;
            case 2:
                updateStudentProfile(student);
                break;
            case 3:
                viewAvailableJobs(student);
                break;
            case 4:
                applyForJob(student);
                break;
            case 5:
                viewMyApplications(student);
                break;
            case 6:
                viewInterviewSchedule(student);
                break;
            case 7:
                viewOffers(student);
                break;
            case 8:
                skillGapAnalysis(student);
                break;
            case 9:
                currentUser = null;
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private static void showCompanyMenu() {
        Company company = (Company) currentUser;
        System.out.println("\n=== Company Menu ===");
        System.out.println("1. View Company Profile");
        System.out.println("2. Post New Job");
        System.out.println("3. View Posted Jobs");
        System.out.println("4. View Applications");
        System.out.println("5. Schedule Interview");
        System.out.println("6. Update Application Status");
        System.out.println("7. Make Job Offer");
        System.out.println("8. View Placement Statistics");
        System.out.println("9. Logout");
        System.out.print("Choose option: ");
        
        int choice = getIntInput();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                viewCompanyProfile(company);
                break;
            case 2:
                postNewJob(company);
                break;
            case 3:
                viewPostedJobs(company);
                break;
            case 4:
                viewApplications(company);
                break;
            case 5:
                scheduleInterview(company);
                break;
            case 6:
                updateApplicationStatus(company);
                break;
            case 7:
                makeJobOffer(company);
                break;
            case 8:
                viewPlacementStats(company);
                break;
            case 9:
                currentUser = null;
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private static void showAdminMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. Register New Student");
        System.out.println("2. Register New Company");
        System.out.println("3. View All Students");
        System.out.println("4. View All Companies");
        System.out.println("5. View All Jobs");
        System.out.println("6. View All Applications");
        System.out.println("7. Generate Placement Report");
        System.out.println("8. Manage Placement Drives");
        System.out.println("9. Logout");
        System.out.print("Choose option: ");
        
        int choice = getIntInput();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                registerStudent();
                break;
            case 2:
                registerCompany();
                break;
            case 3:
                viewAllStudents();
                break;
            case 4:
                viewAllCompanies();
                break;
            case 5:
                viewAllJobs();
                break;
            case 6:
                viewAllApplications();
                break;
            case 7:
                generatePlacementReport();
                break;
            case 8:
                managePlacementDrives();
                break;
            case 9:
                currentUser = null;
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    // Student Methods
    private static void viewStudentProfile(Student student) {
        System.out.println("\n=== Student Profile ===");
        System.out.println("ID: " + student.getId());
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Department: " + student.getDepartment());
        System.out.println("CGPA: " + student.getCgpa());
        System.out.println("Graduation Year: " + student.getGraduationYear());
        System.out.println("Skills: " + student.getSkills());
        System.out.println("Resume: " + (student.getResume() != null ? "Uploaded" : "Not uploaded"));
    }

    private static void updateStudentProfile(Student student) {
        System.out.println("\n=== Update Profile ===");
        System.out.println("1. Add Skill");
        System.out.println("2. Update Resume");
        System.out.println("3. Update CGPA");
        System.out.print("Choose option: ");
        
        int choice = getIntInput();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                System.out.print("Enter skill to add: ");
                String skill = scanner.nextLine();
                student.addSkill(skill);
                System.out.println("Skill added successfully!");
                break;
            case 2:
                System.out.print("Enter resume text/path: ");
                String resume = scanner.nextLine();
                student.setResume(resume);
                System.out.println("Resume updated successfully!");
                break;
            case 3:
                System.out.print("Enter new CGPA: ");
                double cgpa = scanner.nextDouble();
                student.setCgpa(cgpa);
                System.out.println("CGPA updated successfully!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private static void viewAvailableJobs(Student student) {
        System.out.println("\n=== Available Jobs ===");
        List<JobPosting> eligibleJobs = system.getEligibleJobs(student);
        
        if (eligibleJobs.isEmpty()) {
            System.out.println("No jobs available matching your criteria.");
            return;
        }
        
        for (JobPosting job : eligibleJobs) {
            System.out.println("\n" + job);
        }
    }

    private static void applyForJob(Student student) {
        System.out.print("Enter Job ID to apply: ");
        String jobId = scanner.nextLine();
        
        JobPosting job = system.getJobById(jobId);
        if (job == null) {
            System.out.println("Job not found!");
            return;
        }
        
        if (system.applyForJob(student, job)) {
            System.out.println("Application submitted successfully!");
        } else {
            System.out.println("Application failed! You may not meet eligibility criteria or already applied.");
        }
    }

    private static void viewMyApplications(Student student) {
        System.out.println("\n=== My Applications ===");
        List<Application> applications = system.getStudentApplications(student.getId());
        
        if (applications.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }
        
        for (Application app : applications) {
            System.out.println("\n" + app);
        }
    }

    private static void viewInterviewSchedule(Student student) {
        System.out.println("\n=== Interview Schedule ===");
        List<Interview> interviews = system.getStudentInterviews(student.getId());
        
        if (interviews.isEmpty()) {
            System.out.println("No interviews scheduled.");
            return;
        }
        
        for (Interview interview : interviews) {
            System.out.println("\n" + interview);
        }
    }

    private static void viewOffers(Student student) {
        System.out.println("\n=== Job Offers ===");
        List<JobOffer> offers = system.getStudentOffers(student.getId());
        
        if (offers.isEmpty()) {
            System.out.println("No offers received yet.");
            return;
        }
        
        for (JobOffer offer : offers) {
            System.out.println("\n" + offer);
            if (offer.getStatus().equals("PENDING")) {
                System.out.print("Accept this offer? (Y/N): ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("Y")) {
                    offer.accept();
                    System.out.println("Offer accepted!");
                } else if (response.equalsIgnoreCase("N")) {
                    offer.reject();
                    System.out.println("Offer rejected!");
                }
            }
        }
    }

    private static void skillGapAnalysis(Student student) {
        System.out.println("\n=== Skill Gap Analysis ===");
        System.out.print("Enter Job ID for analysis: ");
        String jobId = scanner.nextLine();
        
        JobPosting job = system.getJobById(jobId);
        if (job == null) {
            System.out.println("Job not found!");
            return;
        }
        
        Set<String> studentSkills = student.getSkills();
        Set<String> requiredSkills = job.getRequiredSkills();
        
        Set<String> missingSkills = new HashSet<>(requiredSkills);
        missingSkills.removeAll(studentSkills);
        
        Set<String> matchingSkills = new HashSet<>(requiredSkills);
        matchingSkills.retainAll(studentSkills);
        
        System.out.println("\nJob: " + job.getTitle() + " at " + job.getCompany().getName());
        System.out.println("Required Skills: " + requiredSkills);
        System.out.println("Your Skills: " + studentSkills);
        System.out.println("Matching Skills: " + matchingSkills);
        System.out.println("Missing Skills: " + missingSkills);
        
        double matchPercentage = requiredSkills.isEmpty() ? 100.0 : 
            (matchingSkills.size() * 100.0 / requiredSkills.size());
        System.out.println("Match Percentage: " + String.format("%.2f", matchPercentage) + "%");
        
        if (!missingSkills.isEmpty()) {
            System.out.println("\nRecommended Training:");
            for (String skill : missingSkills) {
                System.out.println("- Learn " + skill);
            }
        }
    }

    // Company Methods
    private static void viewCompanyProfile(Company company) {
        System.out.println("\n=== Company Profile ===");
        System.out.println("ID: " + company.getId());
        System.out.println("Name: " + company.getName());
        System.out.println("Email: " + company.getEmail());
        System.out.println("Industry: " + company.getIndustry());
    }

    private static void postNewJob(Company company) {
        System.out.println("\n=== Post New Job ===");
        System.out.print("Enter Job ID: ");
        String jobId = scanner.nextLine();
        System.out.print("Enter Job Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Job Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Package (LPA): ");
        double salary = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        System.out.print("Enter Minimum CGPA: ");
        double minCgpa = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Eligible Departments (comma-separated): ");
        String deptInput = scanner.nextLine();
        Set<String> departments = new HashSet<>(Arrays.asList(deptInput.split(",")));
        
        System.out.print("Enter Required Skills (comma-separated): ");
        String skillInput = scanner.nextLine();
        Set<String> skills = new HashSet<>(Arrays.asList(skillInput.split(",")));
        
        JobPosting job = new JobPosting(jobId, title, description, company, salary, location, minCgpa, departments, skills);
        system.addJobPosting(job);
        System.out.println("Job posted successfully!");
    }

    private static void viewPostedJobs(Company company) {
        System.out.println("\n=== Posted Jobs ===");
        List<JobPosting> jobs = system.getCompanyJobs(company.getId());
        
        if (jobs.isEmpty()) {
            System.out.println("No jobs posted yet.");
            return;
        }
        
        for (JobPosting job : jobs) {
            System.out.println("\n" + job);
        }
    }

    private static void viewApplications(Company company) {
        System.out.println("\n=== Applications ===");
        System.out.print("Enter Job ID: ");
        String jobId = scanner.nextLine();
        
        List<Application> applications = system.getJobApplications(jobId);
        
        if (applications.isEmpty()) {
            System.out.println("No applications found for this job.");
            return;
        }
        
        for (Application app : applications) {
            System.out.println("\n" + app);
        }
    }

    private static void scheduleInterview(Company company) {
        System.out.print("Enter Application ID: ");
        String appId = scanner.nextLine();
        System.out.print("Enter Interview Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter Interview Time: ");
        String time = scanner.nextLine();
        System.out.print("Enter Interview Location/Link: ");
        String location = scanner.nextLine();
        System.out.print("Enter Interview Type (TECHNICAL/HR/GROUP): ");
        String type = scanner.nextLine();
        
        if (system.scheduleInterview(appId, date, time, location, type)) {
            System.out.println("Interview scheduled successfully!");
        } else {
            System.out.println("Failed to schedule interview!");
        }
    }

    private static void updateApplicationStatus(Company company) {
        System.out.print("Enter Application ID: ");
        String appId = scanner.nextLine();
        System.out.println("Select Status:");
        System.out.println("1. SHORTLISTED");
        System.out.println("2. REJECTED");
        System.out.println("3. ON_HOLD");
        System.out.print("Choose option: ");
        int choice = getIntInput();
        scanner.nextLine();
        
        String status = "";
        switch (choice) {
            case 1: status = "SHORTLISTED"; break;
            case 2: status = "REJECTED"; break;
            case 3: status = "ON_HOLD"; break;
            default:
                System.out.println("Invalid option!");
                return;
        }
        
        if (system.updateApplicationStatus(appId, status)) {
            System.out.println("Application status updated!");
        } else {
            System.out.println("Failed to update status!");
        }
    }

    private static void makeJobOffer(Company company) {
        System.out.print("Enter Application ID: ");
        String appId = scanner.nextLine();
        System.out.print("Enter Package (LPA): ");
        double salary = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Joining Date (YYYY-MM-DD): ");
        String joiningDate = scanner.nextLine();
        System.out.print("Enter Additional Details: ");
        String details = scanner.nextLine();
        
        if (system.makeJobOffer(appId, salary, joiningDate, details)) {
            System.out.println("Job offer sent successfully!");
        } else {
            System.out.println("Failed to send offer!");
        }
    }

    private static void viewPlacementStats(Company company) {
        System.out.println("\n=== Placement Statistics ===");
        List<JobPosting> jobs = system.getCompanyJobs(company.getId());
        
        int totalJobs = jobs.size();
        int totalApplications = 0;
        int totalOffers = 0;
        int acceptedOffers = 0;
        
        for (JobPosting job : jobs) {
            List<Application> apps = system.getJobApplications(job.getJobId());
            totalApplications += apps.size();
            
            for (Application app : apps) {
                List<JobOffer> offers = system.getApplicationOffers(app.getApplicationId());
                totalOffers += offers.size();
                for (JobOffer offer : offers) {
                    if (offer.getStatus().equals("ACCEPTED")) {
                        acceptedOffers++;
                    }
                }
            }
        }
        
        System.out.println("Total Jobs Posted: " + totalJobs);
        System.out.println("Total Applications Received: " + totalApplications);
        System.out.println("Total Offers Made: " + totalOffers);
        System.out.println("Offers Accepted: " + acceptedOffers);
    }

    // Admin Methods
    private static void registerStudent() {
        System.out.println("\n=== Register Student ===");
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Department: ");
        String dept = scanner.nextLine();
        System.out.print("Enter CGPA: ");
        double cgpa = scanner.nextDouble();
        System.out.print("Enter Graduation Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        
        Student student = new Student(id, name, email, dept, cgpa, year);
        system.addStudent(student);
        System.out.println("Student registered successfully!");
    }

    private static void registerCompany() {
        System.out.println("\n=== Register Company ===");
        System.out.print("Enter Company ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Company Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Industry: ");
        String industry = scanner.nextLine();
        
        Company company = new Company(id, name, email, industry);
        system.addCompany(company);
        System.out.println("Company registered successfully!");
    }

    private static void viewAllStudents() {
        System.out.println("\n=== All Students ===");
        List<Student> students = system.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("No students registered.");
            return;
        }
        
        for (Student student : students) {
            System.out.println("\nID: " + student.getId() + " | Name: " + student.getName() + 
                " | Dept: " + student.getDepartment() + " | CGPA: " + student.getCgpa());
        }
    }

    private static void viewAllCompanies() {
        System.out.println("\n=== All Companies ===");
        List<Company> companies = system.getAllCompanies();
        
        if (companies.isEmpty()) {
            System.out.println("No companies registered.");
            return;
        }
        
        for (Company company : companies) {
            System.out.println("\nID: " + company.getId() + " | Name: " + company.getName() + 
                " | Industry: " + company.getIndustry());
        }
    }

    private static void viewAllJobs() {
        System.out.println("\n=== All Jobs ===");
        List<JobPosting> jobs = system.getAllJobs();
        
        if (jobs.isEmpty()) {
            System.out.println("No jobs posted.");
            return;
        }
        
        for (JobPosting job : jobs) {
            System.out.println("\n" + job);
        }
    }

    private static void viewAllApplications() {
        System.out.println("\n=== All Applications ===");
        List<Application> applications = system.getAllApplications();
        
        if (applications.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }
        
        for (Application app : applications) {
            System.out.println("\n" + app);
        }
    }

    private static void generatePlacementReport() {
        System.out.println("\n=== Placement Report ===");
        
        List<Student> students = system.getAllStudents();
        List<JobOffer> allOffers = system.getAllOffers();
        
        int totalStudents = students.size();
        int placedStudents = 0;
        double highestPackage = 0;
        double totalPackage = 0;
        int acceptedOffers = 0;
        
        for (JobOffer offer : allOffers) {
            if (offer.getStatus().equals("ACCEPTED")) {
                placedStudents++;
                acceptedOffers++;
                totalPackage += offer.getPackage();
                if (offer.getPackage() > highestPackage) {
                    highestPackage = offer.getPackage();
                }
            }
        }
        
        double placementPercentage = totalStudents > 0 ? (placedStudents * 100.0 / totalStudents) : 0;
        double averagePackage = acceptedOffers > 0 ? (totalPackage / acceptedOffers) : 0;
        
        System.out.println("Total Students: " + totalStudents);
        System.out.println("Placed Students: " + placedStudents);
        System.out.println("Placement Percentage: " + String.format("%.2f", placementPercentage) + "%");
        System.out.println("Highest Package: " + String.format("%.2f", highestPackage) + " LPA");
        System.out.println("Average Package: " + String.format("%.2f", averagePackage) + " LPA");
        System.out.println("Total Offers Made: " + allOffers.size());
        System.out.println("Offers Accepted: " + acceptedOffers);
    }

    private static void managePlacementDrives() {
        System.out.println("\n=== Manage Placement Drives ===");
        System.out.println("1. Create Placement Drive");
        System.out.println("2. View All Drives");
        System.out.print("Choose option: ");
        
        int choice = getIntInput();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                System.out.print("Enter Drive Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Start Date (YYYY-MM-DD): ");
                String startDate = scanner.nextLine();
                System.out.print("Enter End Date (YYYY-MM-DD): ");
                String endDate = scanner.nextLine();
                System.out.println("Placement Drive '" + name + "' created successfully!");
                break;
            case 2:
                System.out.println("Feature coming soon!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input! Enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
