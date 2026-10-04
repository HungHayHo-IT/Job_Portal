import { test, expect } from "@playwright/test";

test.describe("Job listing functionality", () => {
  test("JOB-01: Job listing page should load successfully", async ({
    page,
  }) => {
    await page.goto("/jobs");

    await expect(
      page.getByRole("heading", { name: "Find Your Perfect Job" })
    ).toBeVisible();

    // Verify the job count section
    await expect(
      page.getByRole("heading", { name: /\d+ Jobs Found/ })
    ).toBeVisible();

    // If jobs exist, the first job title should be visible
    const firstJob = page.locator('a[href^="/jobs/"] h3').first();

    await expect(firstJob).toBeVisible();
  });

  test("JOB-02: Search should filter jobs by title", async ({ page }) => {
    await page.goto("/jobs");

    const firstJob = page.locator('a[href^="/jobs/"] h3').first();

    // Wait until at least one job is available
    await expect(firstJob).toBeVisible();

    // Read a real job title from the page
    const jobTitle = (await firstJob.innerText()).trim();

    const searchInput = page.getByPlaceholder(
      "Job title, company, or keywords..."
    );

    await searchInput.fill(jobTitle);

    await expect(
      page
        .locator('a[href^="/jobs/"] h3')
        .filter({
          hasText: jobTitle,
        })
        .first()
    ).toBeVisible();

    await expect(
      page.getByRole("heading", { name: /\d+ Jobs Found/ })
    ).toBeVisible();
  });

  test("JOB-03: Clicking a job title should open its detail page", async ({
    page,
  }) => {
    await page.goto("/jobs");

    const firstJob = page.locator('a[href^="/jobs/"] h3').first();

    await expect(firstJob).toBeVisible();

    const jobTitle = (await firstJob.innerText()).trim();

    await firstJob.click();

    await expect(page).toHaveURL(/\/jobs\/[^/]+$/);

    await expect(
      page.getByRole("heading", { level: 1, name: jobTitle })
    ).toBeVisible();
  });
});
