#pragma once

#ifdef __cplusplus
extern "C" {
#endif

    float
    ScaleFactor(void);

    static inline int ppp(int x) { return (x * ScaleFactor()); }
    static inline double pppF(double x) { return (x * ScaleFactor()); }

#ifdef __cplusplus
} // extern "C"
#endif
