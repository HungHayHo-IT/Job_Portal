import { defineConfig, devices } from "@playwright/test";

export default defineConfig({
  timeout: 30000,
  testDir: "./tests",
  expect: {
    timeout: 15000, // 2) timeout cho expect(...) (mặc định 5s)
  },

  use: {
    baseURL: "http://localhost:30080/",
    headless: false,
    screenshot: "only-on-failure",
    trace: "on-first-retry",
  },

  projects: [
    {
      name: "chromium",
      use: {
        ...devices["Desktop Chrome"],
      },
    },
  ],
});
