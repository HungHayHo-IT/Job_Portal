# 🚀 Job Portal System - Fullstack Recruitment Platform

Dự án **Job Portal** là một nền tảng tuyển dụng hiện đại, giúp kết nối nhà tuyển dụng và ứng viên một cách tối ưu. Hệ thống tập trung vào trải nghiệm người dùng, tính bảo mật cao và khả năng giám sát hệ thống chuyên sâu.

---

## 🛠 Tech Stack

### 🔹 Backend (Java Ecosystem)
* **Core:** Java 17, Spring Boot 3.2.4.
* **Security:** Spring Security, JSON Web Token (JJWT 0.12.5).
* **Database:** MySQL, Spring Data JPA, Hibernate.
* **Performance:** **Caffeine Cache** (Local Cache với cơ chế TTL).
* **Observability:** Spring Boot Actuator, **OpenTelemetry**, Micrometer.
* **Documentation:** Swagger UI (Springdoc OpenAPI 2.5.0).

### 🔹 Frontend (Modern UI/UX)
* **Framework:** React 19 (Vite).
* **Styling:** **Tailwind CSS v4.0.6**.
* **State Management:** Redux Toolkit (RTK).
* **Integration:** Axios, React Router Dom v7, Stripe JS.

### 🔹 DevOps & Monitoring
* **Infrastructure:** Docker Compose.
* **Monitoring Stack:** Grafana LGTM (Loki, Grafana, Tempo, Mimir).

---

## ✨ Key Features

### 1. Phân quyền người dùng (RBAC)
* Hỗ trợ 3 vai trò: **Candidate**, **Employer**, và **Admin**.
* Xác thực không trạng thái (Stateless) bằng **JWT**.
* Tích hợp cơ chế bảo mật chống các cuộc tấn công phổ biến (CORS, CSRF).

### 2. Quản lý Tuyển dụng & Tìm việc
* **Ứng viên:** Tìm kiếm việc làm thông minh, quản lý Profile, ứng tuyển và theo dõi trạng thái đơn hàng thời gian thực.
* **Nhà tuyển dụng:** Đăng tin, quản lý danh sách ứng viên, cập nhật trạng thái hồ sơ qua Dashboard trực quan.
* **Admin:** Kiểm soát hệ thống, quản lý người dùng và phản hồi Contact.

### 3. Tối ưu Hiệu năng & Giám sát (Observability)
* **Caffeine Caching:** Giảm tải cho Database bằng cách lưu trữ các dữ liệu ít biến động (Roles, Job Categories) với thời gian sống (TTL) linh hoạt.
* **Full Observability:** Sử dụng OpenTelemetry để xuất Metrics, Traces và Logs sang Grafana, giúp phát hiện sớm các "bottleneck" về hiệu năng.
* **AOP Logging:** Tách biệt logic ghi log bằng Aspect Oriented Programming, giữ cho Business Logic luôn "sạch".

