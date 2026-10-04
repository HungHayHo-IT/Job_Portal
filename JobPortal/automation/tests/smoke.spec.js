import { test, expect } from "@playwright/test";

test("Job Portal should open successfully", async ({ page }) => {
  await page.goto("/");

  await expect(page.getByText("Dream Job", { exact: true })).toBeVisible();
});
