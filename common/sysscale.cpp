#include "sysscale.h"

#ifdef _WIN32
#define WIN32_LEAN_AND_MEAN
#include <Windows.h>

FLOAT
GetWindowsDPI()
{
    HDC screen = GetDC(NULL);
    FLOAT dpiX = (FLOAT)GetDeviceCaps(screen, LOGPIXELSX);
    FLOAT dpiY = (FLOAT)GetDeviceCaps(screen, LOGPIXELSY);
    
    return (dpiX + dpiY) / 2;
}

#endif

extern "C"
float
ScaleFactor()
{
#ifdef _WIN32
    static float factor = GetWindowsDPI() / 96.0f;
    return factor;
#else
    return 1.0f;
#endif
}
