#include "csvutil.hpp"

#include <string.h>

#include <string>

using std::string;

QString
csv_parse_field(const QString & field)
{
    string result;
    string source = field.trimmed().toStdString();

    bool quoted = (source.length() >= 2 && *source.begin() == '"' && *(source.end() - 1) == '"');

    if (quoted)
        result += '"';

    for (const char * s = source.c_str() + (quoted ? 1 : 0); s != source.c_str() + source.length() - (quoted ? 1 : 0); ++s)
    {
        char c = *s;
        if (s[0] == '"' && s[1] == '"')
            ++s;

        if (s[0] == '"' && s[1] != '"')
            c = '?';

        if (*s == ',' && !quoted)
            c = ';';

        result += c;
    }

    if (quoted)
        result += '"';

    return QString::fromStdString(result);
}
