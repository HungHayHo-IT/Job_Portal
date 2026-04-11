---------🚀 Job Portal System - Fullstack Recruitment Platform-----------------------
Dự án Job Portal là một nền tảng tuyển dụng toàn diện, kết nối nhà tuyển dụng và ứng viên. Hệ thống được xây dựng với kiến trúc hiện đại, tập trung vào tính bảo mật, hiệu năng cao thông qua cơ chế caching và khả năng giám sát hệ thống (observability).

---------🛠 Tech Stack-------------
-Backend (Java Ecosystem)
  Core: Java 17, Spring Boot 3.2.4.
  Security: Spring Security, JSON Web Token (JJWT 0.12.5).
  Database & Persistence: MySQL, Spring Data JPA, Hibernate.
  Performance: Caffeine Cache (Local Cache với TTL).
  Observability: Spring Boot Actuator, OpenTelemetry, Micrometer.
  Documentation: Swagger UI (Springdoc OpenAPI 2.5.0).
  Others: Lombok, Spring AOP, Validation API.
-Frontend (Modern UI/UX)
  Framework: React 19, Vite.
  Styling: Tailwind CSS v4.0.6 (High performance utility-first CSS).
  State Management: Redux Toolkit (RTK).
  Routing & HTTP: React Router Dom v7, Axios.
  Payment Integration: Stripe JS.
  Icons & UI Components: FontAwesome, Lucide React, React Toastify.

--------✨ Key Features---------------------
1. Phân quyền người dùng (RBAC)
-Hệ thống hỗ trợ 3 vai trò chính: Candidate, Employer, và Admin.
-Xác thực an toàn bằng JWT. Tích hợp cơ chế bảo mật chống các cuộc tấn công phổ biến
2. Quản lý Tuyển dụng & Tìm việc
-Ứng viên: Tìm kiếm công việc, quản lý hồ sơ (Profile), ứng tuyển trực tuyến và theo dõi trạng thái đơn ứng tuyển.
-Nhà tuyển dụng: Đăng tin tuyển dụng, quản lý danh sách ứng viên, cập nhật trạng thái hồ sơ.
-Admin: Quản lý người dùng, công ty và hệ thống tin nhắn liên hệ (Contact).
3. Tối ưu Hiệu năng & Giám sát
-Caffeine Caching: Áp dụng cho các dữ liệu ít thay đổi như danh sách Role (TTL 1 ngày) hoặc các thông tin Job/Company (TTL 10 phút) để giảm tải cho Database.
-Full Observability: Tích hợp OpenTelemetry để xuất (export) Metrics, Traces và Logs sang Grafana LGTM dashboard, giúp theo dõi sức khỏe hệ thống theo thời gian thực.
-AOP Logging: Sử dụng Aspect Oriented Programming để log các sự kiện quan trọng và audit lỗi mà không làm ảnh hưởng đến business logic chính.

