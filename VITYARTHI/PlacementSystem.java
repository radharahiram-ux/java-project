// PlacementSystem.java
import java.util.*;
import java.util.stream.Collectors;

class PlacementSystem {
    private Map<String, Student> students;
    private Map<String, Company> companies;
    private Map<String, Admin> admins;
    private Map<String, JobPosting> jobPostings;
    private Map<String, Application> applications;
    private Map<String, Interview> interviews;
    private Map<String, JobOffer> jobOffers;
    
    private int applicationCounter;
    private int interviewCounter;
    private int offerCounter;

    public PlacementSystem() {
        this.students = new HashMap<>();
        this.companies = new HashMap<>();
        this.admins = new HashMap<>();
        this.jobPostings = new HashMap<>();
        this.applications = new HashMap<>();
        this.interviews = new HashMap<>();
        this.jobOffers = new HashMap<>();
        this.applicationCounter = 1;
        this.interviewCounter = 1;
        this.offerCounter = 1;
    }

    // Student Management
    public void addStudent(Student student) {
        students.put(student.getId(), student);
    }

    public Student getStudentById(String id) {
        return students.get(id);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    // Company Management
    public void addCompany(Company company) {
        companies.put(company.getId(), company);
    }

    public Company getCompanyById(String id) {
        return companies.get(id);
    }

    public List<Company> getAllCompanies() {
        return new ArrayList<>(companies.values());
    }

    // Admin Management
    public void addAdmin(Admin admin) {
        admins.put(admin.getId(), admin);
    }

    public Admin getAdminById(String id) {
        return admins.get(id);
    }

    // Job Posting Management
    public void addJobPosting(JobPosting job) {
        jobPostings.put(job.getJobId(), job);
    }

    public JobPosting getJobById(String jobId) {
        return jobPostings.get(jobId);
    }

    public List<JobPosting> getAllJobs() {
        return new ArrayList<>(jobPostings.values());
    }

    public List<JobPosting> getCompanyJobs(String companyId) {
        return jobPostings.values().stream()
            .filter(job -> job.getCompany().getId().equals(companyId))
            .collect(Collectors.toList());
    }

    public List<JobPosting> getEligibleJobs(Student student) {
        return jobPostings.values().stream()
            .filter(job -> job.isEligible(student))
            .collect(Collectors.toList());
    }

    // Application Management
    public boolean applyForJob(Student student, JobPosting job) {
        // Check if already applied
        for (Application app : applications.values()) {
            if (app.getStudent().getId().equals(student.getId()) &&
                app.getJob().getJobId().equals(job.getJobId())) {
                return false; // Already applied
            }
        }

        // Check eligibility
        if (!job.isEligible(student)) {
            return false;
        }

        String appId = "APP" + String.format("%04d", applicationCounter++);
        Application application = new Application(appId, student, job);
        applications.put(appId, application);
        return true;
    }

    public Application getApplicationById(String applicationId) {
        return applications.get(applicationId);
    }

    public List<Application> getAllApplications() {
        return new ArrayList<>(applications.values());
    }

    public List<Application> getStudentApplications(String studentId) {
        return applications.values().stream()
            .filter(app -> app.getStudent().getId().equals(studentId))
            .collect(Collectors.toList());
    }

    public List<Application> getJobApplications(String jobId) {
        return applications.values().stream()
            .filter(app -> app.getJob().getJobId().equals(jobId))
            .collect(Collectors.toList());
    }

    public boolean updateApplicationStatus(String applicationId, String status) {
        Application app = applications.get(applicationId);
        if (app != null) {
            app.setStatus(status);
            return true;
        }
        return false;
    }

    // Interview Management
    public boolean scheduleInterview(String applicationId, String date, String time,
                                    String location, String type) {
        Application app = applications.get(applicationId);
        if (app == null) {
            return false;
        }

        String interviewId = "INT" + String.format("%04d", interviewCounter++);
        Interview interview = new Interview(interviewId, app, date, time, location, type);
        interviews.put(interviewId, interview);
        
        // Update application status
        app.setStatus("SHORTLISTED");
        return true;
    }

    public List<Interview> getStudentInterviews(String studentId) {
        return interviews.values().stream()
            .filter(interview -> interview.getApplication().getStudent().getId().equals(studentId))
            .collect(Collectors.toList());
    }

    public List<Interview> getCompanyInterviews(String companyId) {
        return interviews.values().stream()
            .filter(interview -> interview.getApplication().getJob()
                .getCompany().getId().equals(companyId))
            .collect(Collectors.toList());
    }

    // Job Offer Management
    public boolean makeJobOffer(String applicationId, double packageLPA,
                               String joiningDate, String details) {
        Application app = applications.get(applicationId);
        if (app == null) {
            return false;
        }

        String offerId = "OFF" + String.format("%04d", offerCounter++);
        JobOffer offer = new JobOffer(offerId, app, packageLPA, joiningDate, details);
        jobOffers.put(offerId, offer);
        return true;
    }

    public List<JobOffer> getStudentOffers(String studentId) {
        return jobOffers.values().stream()
            .filter(offer -> offer.getApplication().getStudent().getId().equals(studentId))
            .collect(Collectors.toList());
    }

    public List<JobOffer> getApplicationOffers(String applicationId) {
        return jobOffers.values().stream()
            .filter(offer -> offer.getApplication().getApplicationId().equals(applicationId))
            .collect(Collectors.toList());
    }

    public List<JobOffer> getAllOffers() {
        return new ArrayList<>(jobOffers.values());
    }

    // Statistics and Reports
    public Map<String, Object> getPlacementStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalStudents = students.size();
        long placedStudents = jobOffers.values().stream()
            .filter(offer -> offer.getStatus().equals("ACCEPTED"))
            .map(offer -> offer.getApplication().getStudent().getId())
            .distinct()
            .count();
        
        double placementPercentage = totalStudents > 0 ? 
            (placedStudents * 100.0 / totalStudents) : 0;
        
        OptionalDouble avgPackage = jobOffers.values().stream()
            .filter(offer -> offer.getStatus().equals("ACCEPTED"))
            .mapToDouble(JobOffer::getPackage)
            .average();
        
        OptionalDouble maxPackage = jobOffers.values().stream()
            .filter(offer -> offer.getStatus().equals("ACCEPTED"))
            .mapToDouble(JobOffer::getPackage)
            .max();
        
        stats.put("totalStudents", totalStudents);
        stats.put("placedStudents", placedStudents);
        stats.put("placementPercentage", placementPercentage);
        stats.put("averagePackage", avgPackage.orElse(0));
        stats.put("highestPackage", maxPackage.orElse(0));
        stats.put("totalJobs", jobPostings.size());
        stats.put("totalApplications", applications.size());
        stats.put("totalOffers", jobOffers.size());
        
        return stats;
    }

    public Map<String, Integer> getDepartmentWisePlacement() {
        Map<String, Integer> deptStats = new HashMap<>();
        
        for (JobOffer offer : jobOffers.values()) {
            if (offer.getStatus().equals("ACCEPTED")) {
                String dept = offer.getApplication().getStudent().getDepartment();
                deptStats.put(dept, deptStats.getOrDefault(dept, 0) + 1);
            }
        }
        
        return deptStats;
    }

    public List<Student> getUnplacedStudents() {
        Set<String> placedStudentIds = jobOffers.values().stream()
            .filter(offer -> offer.getStatus().equals("ACCEPTED"))
            .map(offer -> offer.getApplication().getStudent().getId())
            .collect(Collectors.toSet());
        
        return students.values().stream()
            .filter(student -> !placedStudentIds.contains(student.getId()))
            .collect(Collectors.toList());
    }

    public Map<String, Long> getCompanyWiseHiring() {
        return jobOffers.values().stream()
            .filter(offer -> offer.getStatus().equals("ACCEPTED"))
            .collect(Collectors.groupingBy(
                offer -> offer.getApplication().getJob().getCompany().getName(),
                Collectors.counting()
            ));
    }
}
