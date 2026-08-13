import axios from "axios";
import Cookies from "js-cookie";
import { API_BASE_URL } from "./api";

/**
 * Default API Accept Header
 * This value is used for the Accept header in all API requests
 * Follows the vendor-specific media type format: application/vnd.eazyapp+json;v=1.0
 * Developers can override this in individual requests if needed
 */
export const DEFAULT_ACCEPT_HEADER = "application/vnd.eazyapp+json;v=1.0";

/**
 * Create axios instance with default configuration
 */
const httpClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // Required for CSRF cookie handling
});

/**
 * Public endpoints that don't require authentication
 * These endpoints will not include the Authorization header
 */
const PUBLIC_ENDPOINTS = [
  "/auth/login/public",
  "/auth/register/public",
  "/companies/public",
  "/contacts/public",
];

/**
 * Check if the request URL is a public endpoint
 */
const isPublicEndpoint = (url) => {
  return PUBLIC_ENDPOINTS.some((endpoint) => {
    return (
      url === endpoint ||
      url.startsWith(endpoint + "/") ||
      url.startsWith(endpoint + "?")
    );
  });
};

// Biến lưu trữ CSRF Token trong bộ nhớ tạm để tái sử dụng
let cachedCsrfToken = null;

/**
 * Request Interceptor
 * Automatically adds Accept header and handles CSRF token for non-safe HTTP methods
 */
httpClient.interceptors.request.use(
  async (config) => {
    config.headers.Accept = DEFAULT_ACCEPT_HEADER;

    // Add authentication token for non-public endpoints
    if (!isPublicEndpoint(config.url)) {
      const token = localStorage.getItem("authToken");
      if (token) {
        config.headers["Authorization"] = `Bearer ${token}`;
      }
    }

    // Handle CSRF token for non-safe HTTP methods
    const safeMethods = ["GET", "HEAD", "OPTIONS"];
    if (!safeMethods.includes(config.method.toUpperCase())) {
      // Ưu tiên đọc từ cookie (chạy local) hoặc từ biến cache (khi deploy cross-domain)
      let csrfToken = Cookies.get("XSRF-TOKEN") || cachedCsrfToken;

      // Nếu không có, tiến hành gọi API để lấy
      if (!csrfToken) {
        try {
          const response = await axios.get(
            `${API_BASE_URL}/csrf-token/public`,
            {
              withCredentials: true,
            }
          );

          // Lấy token trực tiếp từ response body thay vì cố gắng đọc lại Cookie
          csrfToken = response.data.token;

          // Lưu token vào cache để sử dụng cho các request sau
          cachedCsrfToken = csrfToken;

          if (!csrfToken) {
            throw new Error("Failed to retrieve CSRF token from API response");
          }
        } catch (error) {
          if (error.response && error.response.status === 404) {
            console.warn(
              "[CSRF Token] Endpoint not found (404), continuing without CSRF token"
            );
          } else {
            console.error("[CSRF Token Error]", error);
            return Promise.reject(error);
          }
        }
      }

      // Add CSRF token to request header
      config.headers["X-XSRF-TOKEN"] = csrfToken;
    }

    return config;
  },
  (error) => {
    console.error("[HTTP Request Error]", error);
    return Promise.reject(error);
  }
);

/**
 * Response Interceptor
 * Handles response transformations and error handling
 */
httpClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response) {
      console.error("[HTTP Response Error]", {
        status: error.response.status,
        data: error.response.data,
        url: error.config.url,
      });

      switch (error.response.status) {
        case 401: {
          const isLoginRequest = error.config.url.includes("/auth/login");
          const isOnLoginPage = window.location.pathname === "/login";

          if (!isLoginRequest && !isOnLoginPage) {
            localStorage.removeItem("authToken");
            localStorage.removeItem("jobPortalUser");
            window.location.href = "/login";
          } else {
            localStorage.removeItem("authToken");
            localStorage.removeItem("jobPortalUser");
          }

          break;
        }
        case 403:
          console.error("Access forbidden");
          break;
        case 404:
          console.error("Resource not found");
          break;
        case 500:
          console.error("Internal server error");
          break;
        default:
          console.error("An error occurred");
      }
    } else if (error.request) {
      console.error("[HTTP No Response]", error.request);
    } else {
      console.error("[HTTP Error]", error.message);
    }

    return Promise.reject(error);
  }
);

/**
 * Helper function to make requests with custom API version
 */
export const withApiVersion = (version) => {
  const acceptHeader = `application/vnd.eazyapp+json;v=${version}`;
  return {
    get: (url, config = {}) =>
      httpClient.get(url, {
        ...config,
        headers: { ...config.headers, Accept: acceptHeader },
      }),
    post: (url, data, config = {}) =>
      httpClient.post(url, data, {
        ...config,
        headers: { ...config.headers, Accept: acceptHeader },
      }),
    put: (url, data, config = {}) =>
      httpClient.put(url, data, {
        ...config,
        headers: { ...config.headers, Accept: acceptHeader },
      }),
    patch: (url, data, config = {}) =>
      httpClient.patch(url, data, {
        ...config,
        headers: { ...config.headers, Accept: acceptHeader },
      }),
    delete: (url, config = {}) =>
      httpClient.delete(url, {
        ...config,
        headers: { ...config.headers, Accept: acceptHeader },
      }),
  };
};

export default httpClient;
