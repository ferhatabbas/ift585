#include "util.h"
#include <cmath>
#include <iterator>

namespace util{
    
double long log2(double long x){
  return log(x)/log(2);
}

bool isPow2(int x){
  return x && (!(x&(x-1)));
}
}
