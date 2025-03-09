package com.example.summonapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.summonapp.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("ADLaM Display")

val customFontFamily = FontFamily(
    Font(googleFont = fontName, fontProvider = provider)
)

val AppTypography = Typography(
    displayLarge = Typography().displayLarge.copy(fontFamily = customFontFamily),
    displayMedium = Typography().displayMedium.copy(fontFamily = customFontFamily),
    displaySmall = Typography().displaySmall.copy(fontFamily = customFontFamily),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = customFontFamily),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = customFontFamily),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = customFontFamily),
    titleLarge = Typography().titleLarge.copy(fontFamily = customFontFamily),
    titleMedium = Typography().titleMedium.copy(fontFamily = customFontFamily),
    titleSmall = Typography().titleSmall.copy(fontFamily = customFontFamily),
    bodyLarge = Typography().bodyLarge.copy(fontFamily = customFontFamily),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = customFontFamily),
    bodySmall = Typography().bodySmall.copy(fontFamily = customFontFamily),
    labelLarge = Typography().labelLarge.copy(fontFamily = customFontFamily),
    labelMedium = Typography().labelMedium.copy(fontFamily = customFontFamily),
    labelSmall = Typography().labelSmall.copy(fontFamily = customFontFamily)
)

