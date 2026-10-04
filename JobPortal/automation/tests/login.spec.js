import { test, expect } from "@playwright/test";
import { Sign } from "crypto";

test.describe("Login functionality", () => {
  test.beforeEach(async ({ page }) => {
    await page.goto("/login");
  });

  test("LOGIN-01: Login page should display correctly", async ({ page }) => {
    const welcomeHeading = page.locator("h1").nth(1);

    await expect(welcomeHeading).toHaveText("Welcome Back");
    await expect(page.getByPlaceholder("Enter your email")).toBeVisible();
    await expect(page.getByPlaceholder("Enter your password")).toBeVisible();
    await expect(page.getByRole("button", { name: "Sign in" })).toBeVisible();
  });

  test("LOGIN-02: Email and password fields are required", async ({ page }) => {
    const emailInput = page.getByPlaceholder("Enter your email");
    const passwordInput = page.getByPlaceholder("Enter your password");

    await expect(emailInput).toHaveAttribute("required", "");
    await expect(passwordInput).toHaveAttribute("required", "");

    await expect(emailInput).toHaveValue("");
    await expect(passwordInput).toHaveValue("");
  });

  test("LOGIN-03: Invalid credentials should show an error", async ({
    page,
  }) => {
    await page
      .getByPlaceholder("Enter your email")
      .fill("notfound@example.com");

    await page
      .getByPlaceholder("Enter your password")
      .fill("WrongPassword123!");

    await page.getByRole("button", { name: "Sign In" }).click();

    const errorMessage = page.getByText("An unexpected error occurred");

    await expect(errorMessage).toBeVisible();
  });

  test("JOB-04: Guest should be redirected to login when saving a job", async ({
    page,
  }) => {
    await page.goto("/jobs");

    const firstJob = page.locator('a[href^="/jobs/"] h3').first();

    await expect(firstJob).toBeVisible();

    const jobCard = firstJob.locator(
      'xpath=ancestor::div[contains(@class, "group")]'
    );

    await expect(
      jobCard.getByRole("button", { name: /Login to Save/ })
    ).toBeVisible();

    await jobCard.getByRole("button", { name: /Login to Save/ }).click();

    await expect(page).toHaveURL(/\/login$/);
  });
});
