CREATE DATABASE IF NOT EXISTS mediscan_db;
USE mediscan_db;

-- USERS
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255),
    phone VARCHAR(15),
    role ENUM('patient', 'doctor', 'admin') DEFAULT 'patient',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (name, email, password_hash, phone, role) VALUES
('Anusha SN', 'anusha@gmail.com', 'hash_anu123', '9876543210', 'patient'),
('Rahul Mehta', 'rahul.mehta@gmail.com', 'hash_rahul', '9998877665', 'patient'),
('Neha Patil', 'neha.patil@gmail.com', 'hash_neha', '9988776655', 'patient'),
('Ramesh Gowda', 'ramesh.gowda@gmail.com', 'hash_ramesh', '9876012345', 'patient'),
('Priya Nair', 'priya.nair@gmail.com', 'hash_priya', '9876023456', 'patient'),
('Suresh Kumar', 'suresh.kumar@gmail.com', 'hash_suresh', '9876034567', 'patient'),
('Meena Das', 'meena.das@gmail.com', 'hash_meena', '9876045678', 'patient'),
('Vikram Rao', 'vikram.rao@gmail.com', 'hash_vikram', '9876056789', 'patient'),
('Kavita Menon', 'kavita.menon@gmail.com', 'hash_kavita', '9876067890', 'patient'),
('Ajay Singh', 'ajay.singh@gmail.com', 'hash_ajay', '9876078901', 'patient'),
('Dr. Ravi Kumar', 'ravi.kumar@cityhospital.com', 'hash_docRavi', '9123456789', 'doctor'),
('Dr. Priya Sharma', 'priya.sharma@cityhospital.com', 'hash_docPriya', '9876123456', 'doctor'),
('Dr. Anil Verma', 'anil.verma@cityhospital.com', 'hash_docAnil', '9876223456', 'doctor'),
('Dr. Sneha Iyer', 'sneha.iyer@cityhospital.com', 'hash_docSneha', '9876334567', 'doctor'),
('Admin Hospital', 'admin@cityhospital.com', 'hash_admin', '9000012345', 'admin');

CREATE TABLE doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    specialization VARCHAR(100),
    availability VARCHAR(100)
);

INSERT INTO doctors (name, specialization, availability) VALUES
('Dr. Ravi Kumar', 'Cardiology', 'Mon-Fri: 10am-5pm'),
('Dr. Priya Sharma', 'Dermatology', 'Tue-Sat: 9am-3pm'),
('Dr. Anil Verma', 'Orthopedic', 'Mon-Fri: 11am-6pm'),
('Dr. Sneha Iyer', 'Pediatrics', 'Mon-Sat: 9am-4pm'),
('Dr. Rohan Desai', 'Neurology', 'Mon-Thu: 10am-2pm'),
('Dr. Kavya Reddy', 'ENT', 'Mon-Sat: 9am-1pm'),
('Dr. Manish Gupta', 'General Medicine', 'Mon-Sat: 10am-6pm'),
('Dr. Shweta Patel', 'Gynecology', 'Tue-Fri: 9am-2pm'),
('Dr. Sanjay Bhat', 'Urology', 'Mon-Fri: 11am-4pm'),
('Dr. Lakshmi Narayan', 'Ophthalmology', 'Mon-Sat: 9am-5pm');

CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    doctor_id INT,
    appointment_date DATE,
    time_slot VARCHAR(20),
    status VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

INSERT INTO appointments (user_id, doctor_id, appointment_date, time_slot, status) VALUES
(1, 1, '2025-10-22', '10:30 AM', 'booked'),
(2, 2, '2025-10-23', '11:00 AM', 'booked'),
(3, 3, '2025-10-24', '01:00 PM', 'completed'),
(4, 4, '2025-10-24', '02:30 PM', 'booked'),
(5, 5, '2025-10-25', '12:00 PM', 'cancelled'),
(6, 6, '2025-10-25', '09:30 AM', 'booked'),
(7, 7, '2025-10-26', '03:00 PM', 'completed'),
(8, 8, '2025-10-26', '11:00 AM', 'booked'),
(9, 9, '2025-10-27', '04:00 PM', 'booked'),
(10, 10, '2025-10-28', '10:00 AM', 'booked');



CREATE TABLE pharmacy (
    pharmacy_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    address VARCHAR(255),
    contact VARCHAR(20),
    delivery_available BOOLEAN
);

INSERT INTO pharmacy (name, address, contact, delivery_available) VALUES
('MediCare Plus', 'MG Road, Bangalore', '080-2223344', TRUE),
('HealthKart Pharmacy', 'BTM Layout, Bangalore', '080-4433221', TRUE),
('City Life Pharmacy', 'Indiranagar, Bangalore', '080-5557788', FALSE),
('Apollo Pharmacy', 'HSR Layout, Bangalore', '080-8889990', TRUE),
('MedPlus Hub', 'Whitefield, Bangalore', '080-2345123', TRUE),
('QuickMeds', 'Koramangala, Bangalore', '080-7654321', TRUE),
('Wellness Forever', 'Jayanagar, Bangalore', '080-9988776', FALSE),
('GreenPharma', 'Hebbal, Bangalore', '080-2345678', TRUE),
('LifeCare Pharmacy', 'Electronic City, Bangalore', '080-1112223', TRUE),
('CarePlus Drugs', 'Rajajinagar, Bangalore', '080-5556667', FALSE);

-- ⚠️ Drop old table (only do this if you don't need existing data)
DROP TABLE IF EXISTS mediconnect_bridge;

-- 🏥 Create new full-featured table
CREATE TABLE mediconnect_bridge (
    bridge_id INT AUTO_INCREMENT PRIMARY KEY,
    hospital_name VARCHAR(100) NOT NULL,
    available_beds INT DEFAULT 0,
    available_icu INT DEFAULT 0,
    available_ambulance INT DEFAULT 0,
    available_ventilators INT DEFAULT 0,
    address VARCHAR(255),
    contact_number VARCHAR(20),
    specialties VARCHAR(255)
);

-- 🩺 Insert sample hospitals with address, contact, and specialties
INSERT INTO mediconnect_bridge 
(hospital_name, available_beds, available_icu, available_ambulance, available_ventilators, address, contact_number, specialties)
VALUES
('CityCare Hospital', 2, 1, 3, 5, '123 MG Road, Bengaluru', '080-22334455', 'Cardiology, Neurology, Pediatrics'),
('GreenLeaf Medical Center', 0, 0, 1, 2, '45 JP Nagar, Bengaluru', '080-33445566', 'General Medicine, ENT, Orthopedics'),
('Sunshine Hospital', 5, 2, 2, 3, '10 Residency Road, Bengaluru', '080-99887766', 'Gynecology, Pediatrics, Orthopedics'),
('Apollo Specialty Clinic', 1, 1, 0, 1, 'Apollo Towers, Indiranagar, Bengaluru', '080-77889900', 'Oncology, Cardiology, Radiology'),
('Fortis Hospital', 4, 3, 2, 4, 'Bannerghatta Road, Bengaluru', '080-60004444', 'Cardiology, Neurology, Surgery'),
('Manipal Hospital', 7, 2, 3, 6, 'Old Airport Road, Bengaluru', '080-25023500', 'Multi-specialty, Nephrology, Pediatrics'),
('Narayana Health', 3, 2, 2, 5, 'Bommasandra Industrial Area, Bengaluru', '080-71222222', 'Cardiac Sciences, Oncology, Orthopedics'),
('Sakra World Hospital', 6, 4, 3, 7, 'Outer Ring Road, Marathahalli, Bengaluru', '080-49694969', 'Orthopedics, Neurology, Urology'),
('Columbia Asia', 2, 1, 2, 3, 'Whitefield Main Road, Bengaluru', '080-67355000', 'Internal Medicine, Pediatrics, ENT'),
('Vijaya Hospital', 1, 0, 1, 2, 'Basavanagudi, Bengaluru', '080-26570707', 'Dermatology, Gynecology, Dentistry');


CREATE TABLE patient_redirects (
    redirect_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    from_hospital_id INT,
    to_hospital_id INT,
    reason VARCHAR(255)
);

INSERT INTO patient_redirects (patient_id, from_hospital_id, to_hospital_id, reason) VALUES
(1, 2, 3, 'ICU unavailable'),
(2, 1, 4, 'Emergency transfer'),
(3, 5, 6, 'Specialist not available'),
(4, 3, 7, 'High demand'),
(5, 6, 8, 'Better equipment'),
(6, 4, 2, 'No ventilator available'),
(7, 8, 1, 'Location preference'),
(8, 9, 5, 'Insurance not covered'),
(9, 10, 3, 'Ambulance required'),
(10, 7, 4, 'Doctor on leave');


CREATE TABLE chatbot_log (
    chat_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    user_query TEXT,
    bot_response TEXT
);

INSERT INTO chatbot_log (user_id, user_query, bot_response) VALUES
(1, 'I have chest pain', 'You should consult a cardiologist immediately.'),
(2, 'Book appointment for skin rash', 'Dr. Priya Sharma is available tomorrow 11AM.'),
(3, 'My child has fever', 'Dr. Sneha Iyer can assist.'),
(4, 'Need ortho specialist', 'Dr. Anil Verma has a slot at 1PM.'),
(5, 'Which hospital has ventilators?', 'Sunshine Hospital currently has 3 available.'),
(6, 'Where is nearest pharmacy?', 'MediCare Plus, MG Road, is 1.2km away.'),
(7, 'Can I reschedule my appointment?', 'Yes, please choose a new time.'),
(8, 'Show my last report', 'Your report is ready at hospitalreports.com/reports/vikram.pdf.'),
(9, 'How many ICU beds in Fortis?', 'Fortis has 3 ICU beds available.'),
(10, 'Talk in Hindi', 'ज़रूर, मैं हिंदी में बात कर सकता हूँ!');

CREATE TABLE IF NOT EXISTS register (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    gender ENUM('Male', 'Female', 'Other') DEFAULT 'Other',
    age INT CHECK (age >= 0),
    phone VARCHAR(15),
    address VARCHAR(255),
    role ENUM('USER', 'DOCTOR', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO register (full_name, email, password, gender, age, phone, address, role) VALUES
('Anusha SN', 'anusha@gmail.com', 'anusha123', 'Female', 24, '9876543210', 'Bangalore, India', 'USER'),
('Ravi Kumar', 'ravi.kumar@gmail.com', 'ravi123', 'Male', 28, '9123456780', 'Hyderabad, India', 'USER'),
('Priya Sharma', 'priya.sharma@gmail.com', 'priya123', 'Female', 26, '9845098765', 'Delhi, India', 'USER'),
('Arjun Reddy', 'arjun.reddy@gmail.com', 'arjun123', 'Male', 30, '9700012345', 'Chennai, India', 'USER'),
('Sneha Patel', 'sneha.patel@gmail.com', 'sneha123', 'Female', 25, '9001122334', 'Ahmedabad, India', 'USER'),
('Rahul Mehta', 'rahul.mehta@gmail.com', 'rahul123', 'Male', 29, '8803344556', 'Pune, India', 'USER'),
('Divya Rao', 'divya.rao@gmail.com', 'divya123', 'Female', 27, '9911223344', 'Kochi, India', 'USER'),
('Vikram Singh', 'vikram.singh@gmail.com', 'vikram123', 'Male', 31, '8776655443', 'Jaipur, India', 'USER'),
('Meera Nair', 'meera.nair@gmail.com', 'meera123', 'Female', 24, '9099887766', 'Thiruvananthapuram, India', 'USER'),
('Dr. Karthik', 'dr.karthik@gmail.com', 'karthik123', 'Male', 35, '9445566778', 'Chennai, India', 'ADMIN');

SELECT * FROM register;


SELECT * FROM doctors;

SELECT * FROM users;

UPDATE users 
SET password = 'hash_arun123' 
WHERE email = 'rahul.mehta@gmail.com';




UPDATE users
SET name = 'Akshay'
WHERE email = 'anusha@gmail.com';

UPDATE users
SET email = 'akshay@gmail.com'
WHERE user_id = 1;

DROP TABLE IF EXISTS appointments;

CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    doctor_id INT,
    patient_name VARCHAR(100),
    doctor_name VARCHAR(100),
    appointment_date DATE,
    time_slot VARCHAR(20),
    status VARCHAR(20),
    notes VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

INSERT INTO appointments 
(user_id, doctor_id, patient_name, doctor_name, appointment_date, time_slot, status, notes) 
VALUES
(1, 1, 'Anusha SN', 'Dr. Ravi Kumar', '2025-10-22', '10:30 AM', 'cancelled', 'Follow-up needed'),
(2, 2, 'Rahul Mehta', 'Dr. Priya Sharma', '2025-10-23', '11:00 AM', 'booked', 'Skin rash consultation'),
(3, 3, 'Neha Patil', 'Dr. Anil Verma', '2025-10-24', '01:00 PM', 'completed', 'Post-surgery checkup'),
(4, 4, 'Ramesh Gowda', 'Dr. Sneha Iyer', '2025-10-24', '02:30 PM', 'booked', 'Child vaccination'),
(5, 5, 'Priya Nair', 'Dr. Rohan Desai', '2025-10-25', '12:00 PM', 'cancelled', 'Rescheduled due to travel'),
(6, 6, 'Suresh Kumar', 'Dr. Kavya Reddy', '2025-10-25', '09:30 AM', 'booked', 'ENT consultation'),
(7, 7, 'Meena Das', 'Dr. Manish Gupta', '2025-10-26', '03:00 PM', 'completed', 'Routine checkup'),
(8, 8, 'Vikram Rao', 'Dr. Shweta Patel', '2025-10-26', '11:00 AM', 'booked', 'Gynecology advice for wife'),
(9, 9, 'Kavita Menon', 'Dr. Sanjay Bhat', '2025-10-27', '04:00 PM', 'booked', 'Kidney health checkup'),
(10, 10, 'Ajay Singh', 'Dr. Lakshmi Narayan', '2025-10-28', '10:00 AM', 'booked', 'Eye test required');

select * from doctors;

CREATE TABLE IF NOT EXISTS medicines (
  medicine_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150),
  category VARCHAR(100),
  price DOUBLE,
  stock INT,
  description TEXT
);

INSERT INTO medicines (name, category, price, stock, description) VALUES
('Paracetamol 500mg', 'Analgesic', 20.0, 200, 'Relief from fever and pain'),
('Amoxicillin 500mg', 'Antibiotic', 45.0, 120, 'For bacterial infections'),
('Cetirizine 10mg', 'Antihistamine', 30.0, 150, 'Allergy relief'),
('Vitamin D 1000IU', 'Supplement', 150.0, 80, 'Bone support'),
('Azithromycin 250mg', 'Antibiotic', 60.0, 60, 'Broad spectrum antibiotic');

SELECT * FROM medicines;

SELECT * FROM pharmacy;
 
DESC pharmacy;

UPDATE pharmacy
SET delivery_available = 1
WHERE pharmacy_id > 0;

SET SQL_SAFE_UPDATES = 0;
UPDATE pharmacy SET delivery_available = 1;
SET SQL_SAFE_UPDATES = 1;

SELECT name, delivery_available FROM pharmacy;

SELECT user_id, name, email, role FROM users;

SELECT * FROM users;


DROP TABLE IF EXISTS qr_codes;
DROP TABLE IF EXISTS qr_report;

CREATE TABLE qr_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    report_url VARCHAR(255),
    user_id VARCHAR(50),
    diagnosis VARCHAR(255),
    treatment VARCHAR(255),
    prescriptions VARCHAR(255),
    doctor_notes VARCHAR(255)
);

-- Sample Data
INSERT INTO qr_report (name, report_url, user_id, diagnosis, treatment, prescriptions, doctor_notes)
VALUES
('Anu', 'anu.pdf', '1', 'Flu and Cold', 'Rest and hydration', 'Paracetamol 500mg, Vitamin C', 'Patient should follow up in 1 week'),
('Rahul', 'rahul.pdf', '2', 'High Blood Pressure', 'Lifestyle changes and medication', 'Amlodipine 5mg', 'Monitor BP daily'),
('Neha', 'neha.pdf', '3', 'Diabetes Type 2', 'Diet control and insulin therapy', 'Metformin 500mg, Insulin', 'Regular blood sugar check'),
('Ramesh', 'ramesh.pdf', '4', 'Fractured Arm', 'Casting and physiotherapy', 'Painkillers as needed', 'Avoid lifting heavy objects'),
('Priya', 'priya.pdf', '5', 'Migraine', 'Medication and rest', 'Sumatriptan 50mg', 'Track triggers and avoid stress');



select * from qr_report;

DESCRIBE qr_report;









































