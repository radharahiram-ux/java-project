// User.java
import java.util.*;
import java.time.*;

abstract class User {
    protected String id;
    protected String name;
    protected String email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}

// Student.java


class Student extends User {
    private String department;
    private double cgpa;
    private int graduationYear;
    private Set<String> skills;
    private String resume;

    public Student(String id, String name, String email, String department, double cgpa, int graduationYear) {
        super(id, name, email);
        this.department = department;
        this.cgpa = cgpa;
        this.graduationYear = graduationYear;
        this.skills = new HashSet<>();
        this.resume = null;
    }

    public String getDepartment() { return department; }
    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }
    public int getGraduationYear() { return graduationYear; }
    public Set<String> getSkills() { return new HashSet<>(skills); }
    public void addSkill(String skill) { skills.add(skill); }
    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }
}

// Company.java
class Company extends User {
    private String industry;

    public Company(String id, String name, String email, String industry) {
        super(id, name, email);
        this.industry = industry;
    }

    public String getIndustry() { return industry; }
}

// Admin.java
class Admin extends User {
    public Admin(String id, String name, String email) {
        super(id, name, email);
    }
}

// JobPosting.java

class JobPosting {
    private String jobId;
    private String title;
    private String description;
    private Company company;
    private double packageLPA;
    private String location;
    private double minCgpa;
    private Set<String> eligibleDepartments;
    private Set<String> requiredSkills;
    private String postDate;

    public JobPosting(String jobId, String title, String description, Company company,
                     double packageLPA, String location, double minCgpa,
                     Set<String> eligibleDepartments, Set<String> requiredSkills) {
        this.jobId = jobId;
        this.title = title;
        this.description = description;
        this.company = company;
        this.packageLPA = packageLPA;
        this.location = location;
        this.minCgpa = minCgpa;
        this.eligibleDepartments = new HashSet<>(eligibleDepartments);
        this.requiredSkills = new HashSet<>(requiredSkills);
        this.postDate = java.time.LocalDate.now().toString();
    }

    public String getJobId() { return jobId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Company getCompany() { return company; }
    public double getPackageLPA() { return packageLPA; }
    public String getLocation() { return location; }
    public double getMinCgpa() { return minCgpa; }
    public Set<String> getEligibleDepartments() { return new HashSet<>(eligibleDepartments); }
    public Set<String> getRequiredSkills() { return new HashSet<>(requiredSkills); }

    public boolean isEligible(Student student) {
        if (student.getCgpa() < minCgpa) return false;
        if (!eligibleDepartments.contains(student.getDepartment())) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Job ID: " + jobId + "\n" +
               "Title: " + title + "\n" +
               "Company: " + company.getName() + "\n" +
               "Package: " + packageLPA + " LPA\n" +
               "Location: " + location + "\n" +
               "Min CGPA: " + minCgpa + "\n" +
               "Eligible Departments: " + eligibleDepartments + "\n" +
               "Required Skills: " + requiredSkills + "\n" +
               "Description: " + description;
    }
}

// Application.java

class Application {
    private String applicationId;
    private Student student;
    private JobPosting job;
    private String status; // APPLIED, SHORTLISTED, REJECTED, ON_HOLD
    private LocalDateTime appliedDate;

    public Application(String applicationId, Student student, JobPosting job) {
        this.applicationId = applicationId;
        this.student = student;
        this.job = job;
        this.status = "APPLIED";
        this.appliedDate = LocalDateTime.now();
    }

    public String getApplicationId() { return applicationId; }
    public Student getStudent() { return student; }
    public JobPosting getJob() { return job; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getAppliedDate() { return appliedDate; }

    @Override
    public String toString() {
        return "Application ID: " + applicationId + "\n" +
               "Student: " + student.getName() + " (" + student.getId() + ")\n" +
               "Job: " + job.getTitle() + " at " + job.getCompany().getName() + "\n" +
               "Status: " + status + "\n" +
               "Applied Date: " + appliedDate.toString();
    }
}

// Interview.java
class Interview {
    private String interviewId;
    private Application application;
    private String date;
    private String time;
    private String location;
    private String type; // TECHNICAL, HR, GROUP
    private String status; // SCHEDULED, COMPLETED, CANCELLED

    public Interview(String interviewId, Application application, String date,
                    String time, String location, String type) {
        this.interviewId = interviewId;
        this.application = application;
        this.date = date;
        this.time = time;
        this.location = location;
        this.type = type;
        this.status = "SCHEDULED";
    }

    public String getInterviewId() { return interviewId; }
    public Application getApplication() { return application; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Interview ID: " + interviewId + "\n" +
               "Job: " + application.getJob().getTitle() + "\n" +
               "Company: " + application.getJob().getCompany().getName() + "\n" +
               "Type: " + type + "\n" +
               "Date: " + date + "\n" +
               "Time: " + time + "\n" +
               "Location: " + location + "\n" +
               "Status: " + status;
    }
}

// JobOffer.java
class JobOffer {
    private String offerId;
    private Application application;
    private double packageLPA;
    private String joiningDate;
    private String details;
    private String status; // PENDING, ACCEPTED, REJECTED

    public JobOffer(String offerId, Application application, double packageLPA,
                   String joiningDate, String details) {
        this.offerId = offerId;
        this.application = application;
        this.packageLPA = packageLPA;
        this.joiningDate = joiningDate;
        this.details = details;
        this.status = "PENDING";
    }

    public String getOfferId() { return offerId; }
    public Application getApplication() { return application; }
    public double getPackage() { return packageLPA; }
    public String getJoiningDate() { return joiningDate; }
    public String getDetails() { return details; }
    public String getStatus() { return status; }
    
    public void accept() { this.status = "ACCEPTED"; }
    public void reject() { this.status = "REJECTED"; }

    @Override
    public String toString() {
        return "Offer ID: " + offerId + "\n" +
               "Job: " + application.getJob().getTitle() + "\n" +
               "Company: " + application.getJob().getCompany().getName() + "\n" +
               "Package: " + packageLPA + " LPA\n" +
               "Joining Date: " + joiningDate + "\n" +
               "Details: " + details + "\n" +
               "Status: " + status;
    }
}
