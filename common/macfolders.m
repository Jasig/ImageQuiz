#import <Foundation/Foundation.h>

#include <string.h>

void
mac_resource_folder(char * buffer, size_t size)
{
    *buffer = '\0';
    
    NSBundle * bundle = [NSBundle mainBundle];
    if (bundle == nil)
    {
        NSLog(@"bunlde is nil. unable to find resource folder");
    }
    else 
    {
        NSString * path = [bundle resourcePath];
        snprintf(buffer, size, "%s/", [path UTF8String]);
    }
}


