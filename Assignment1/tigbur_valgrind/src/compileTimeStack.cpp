#include <string>
#include <vector>
#include <iostream> 

using namespace std;

void compileTimeStack()
{
    int a = 1;
    int b = 2;
    cout << &a <<std::endl;
    cout << &b <<std::endl;
    int j[5] = {1,2,3,4,5};
    cout << j <<std::endl;
    int x[100];
    cout << x <<std::endl;
    x[-5] = 10;
    cout << x << std::endl;    
    cout << &j <<std::endl;
    for(int* cur_j = j;cur_j < &j[5];cur_j++)
        cout << *cur_j << std::endl;        
}
