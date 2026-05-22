package com.project.spendtrackrq.utils

import androidx.compose.ui.tooling.preview.Preview

/**
 * A collection of previews that represent common device configurations.
 * This annotation can be applied to any @Composable function to see how it looks
 * on a small phone, a standard phone, a foldable, and a tablet.
 */
@Preview(
    name = "Small Phone",
    group = "Device Previews",
    device = "spec:width=360dp,height=640dp,dpi=411"
)
@Preview(
    name = "Standard Phone",
    group = "Device Previews",
    device = "id:pixel_6" // A common modern device
)
@Preview(
    name = "Large Screen (Foldable)",
    group = "Device Previews",
    device = "spec:width=673dp,height=841dp,dpi=480" // Represents a foldable like Pixel Fold unfolded
)
@Preview(
    name = "Tablet",
    group = "Device Previews",
    device = "spec:width=1280dp,height=800dp,dpi=240" // A standard 10-inch tablet
)
annotation class DevicePreviews


/**
 * A collection of previews for testing different user settings like Dark Mode
 * and increased font size for accessibility.
 */
@Preview(
    name = "Dark Mode",
    group = "UI Mode Previews",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Large Font Size",
    group = "Font Scale Previews",
    fontScale = 1.5f, showSystemUi = false
)
annotation class UserSettingsPreviews


/**
 * A comprehensive set of all previews for thorough testing.
 * Combines device previews and user settings previews.
 */
@DevicePreviews
@UserSettingsPreviews
annotation class AllPreviews