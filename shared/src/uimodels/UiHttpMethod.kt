package com.techullurgy.dosthman.shared.uimodels

import androidx.compose.ui.graphics.Color

enum class UiHttpMethod(val color: Color) {
    Get(Color(0xFF49DF95)),     // Mint Green (Contrast 10.7:1 vs #1A1816)
    Post(Color(0xFFFFB74D)),    // Warm Amber (Contrast 10.6:1 vs #1A1816)
    Put(Color(0xFF64D2FF)),     // Bright Sky Blue (Contrast 10.7:1 vs #1A1816)
    Patch(Color(0xFF2DD4BF)),   // Vibrant Teal (Contrast 9.9:1 vs #1A1816)
    Delete(Color(0xFFFF6B6B)),  // Coral Red (Contrast 6.6:1 vs #1A1816)
    Head(Color(0xFFC084FC)),    // Lavender Purple (Contrast 7.0:1 vs #1A1816)
    Options(Color(0xFF818CF8))  // Soft Indigo (Contrast 6.2:1 vs #1A1816)
}
