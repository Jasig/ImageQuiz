#include <QApplication>
#include <QDir>
#include "quizapp.h"

#ifdef _WIN32
    #define WIN32_LEAND_AND_MEAN
    #include <Windows.h>
#endif

#ifdef _WIN32
void
SetupDPIAwarenessPreWin81()
{
    HINSTANCE user32Dll = LoadLibrary(L"user32.dll");

    if (!user32Dll)
    {
        return;
    }

    typedef BOOL (WINAPI* SetProcessDPIAware_t)(void);
    SetProcessDPIAware_t SetProcessDPIAware = (SetProcessDPIAware_t)(GetProcAddress(user32Dll, "SetProcessDPIAware"));

    if (SetProcessDPIAware)
        SetProcessDPIAware();

    FreeLibrary(user32Dll);
}

void
SetupDPIAwareness()
{
    HMODULE shCoreDll = LoadLibraryW(L"Shcore.dll");
    if (shCoreDll)
    {
        enum ProcessDpiAwareness
        {
            ProcessDpiUnaware         = 0,
            ProcessSystemDpiAware     = 1,
            ProcessPerMonitorDpiAware = 2
        };

        typedef HRESULT (WINAPI* SetProcessDpiAwareness_t)(ProcessDpiAwareness);
        SetProcessDpiAwareness_t SetProcessDpiAwareness = (SetProcessDpiAwareness_t)(GetProcAddress(shCoreDll, "SetProcessDpiAwareness"));

        if (!SetProcessDpiAwareness || SetProcessDpiAwareness(ProcessPerMonitorDpiAware) == E_INVALIDARG)
        {
            SetupDPIAwarenessPreWin81();
        }

        FreeLibrary(shCoreDll);
    }
    else
        SetupDPIAwarenessPreWin81();
}
#endif

int main( int argc, char *argv[] )
{
#ifdef _WIN32
    SetupDPIAwareness();
#endif

    qputenv("QT_AUTO_SCREEN_SCALE_FACTOR", "1");

    QApplication app(argc, argv);

#ifdef _WIN32
    QFont font = qApp->font();
    font.setPixelSize(11);
    qApp->setFont(font);
#endif

    QuizApp quizApp;
    quizApp.show();
    return app.exec();
}
