import { test, expect } from "@playwright/test";

// Dữ liệu mặc định hợp lệ, test nào cần sai field nào thì ghi đè field đó
const validUser = {
  name: "Hung Automation",
  email: "hung.validation@example.com",
  phone: "0912345678",
  password: "TestPassword123",
  confirmPassword: "TestPassword123",
};

async function fillRegisterForm(page, data) {
  await page.getByPlaceholder("Enter your full name").fill(data.name);
  await page.getByPlaceholder("Enter your email").fill(data.email);
  await page.getByPlaceholder("Enter 10-digit mobile number").fill(data.phone);
  await page.getByPlaceholder("Create password").fill(data.password);
  await page.getByPlaceholder("Confirm password").fill(data.confirmPassword);
}

test.describe("Registration functionality", () => {
  test.beforeEach(async ({ page }) => {
    await page.goto("/register");
  });

  test("REG-01: Register with valid information", async ({ page }) => {
    // Email mới cho mỗi lần chạy để không bị trùng
    const email = `e2e-${Date.now()}@example.com`;

    await fillRegisterForm(page, { ...validUser, email });
    await page.getByRole("button", { name: "Create Account" }).click();

    await expect(page).toHaveURL(/\/login$/);
    await expect(
      page.getByText(
        /registration successful|please login with your credentials/i
      )
    ).toBeVisible();
  });

  test("REG-02: Name shorter than 5 characters should show validation error", async ({
    page,
  }) => {
    await fillRegisterForm(page, { ...validUser, name: "Hung" });
    await page.getByRole("button", { name: "Create Account" }).click();

    await expect(
      page.getByText("Name must be at least 5 characters", { exact: true })
    ).toBeVisible();
    await expect(page).toHaveURL(/\/register$/);
  });

  test("REG-03: Mismatched passwords should show validation error", async ({
    page,
  }) => {
    await fillRegisterForm(page, {
      ...validUser,
      confirmPassword: "DifferentPassword123",
    });
    await page.getByRole("button", { name: "Create Account" }).click();

    await expect(
      page.getByText("Passwords do not match", { exact: true })
    ).toBeVisible();
    await expect(page).toHaveURL(/\/register$/);
  });
});
