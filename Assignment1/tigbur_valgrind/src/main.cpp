#include <vector>
#include <iostream> 


using namespace std;



void memoryLeakExample()
{
    int* x = new int(3);
    *x += 1;
    cout << x <<std::endl;
    cout << *x <<std::endl;
}


void ilegalHeapAccess()
{
    int* x = new int[100];
    x[100] = 10;
    cout << &x[100] <<std::endl;
    cout << x[100] <<std::endl;
}

void ilegalStackAccess()
{
    int x[100];
    x[-100] = 10;
    cout << &x[-100] <<std::endl;
    cout << x[-100] <<std::endl;
}


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
    x[1000] = 10;
    x[-1000] = 10;
    // x[-5] = 10;
    x[100] = 10;
    for(int* cur_j = j;cur_j < &j[5];cur_j++)
        cout << *cur_j << std::endl;        
}

void nested_addresses(int x)
{
    cout << &x <<std::endl;
    x--;
    if (x > 0)
        nested_addresses(x);
}


void nested_arrs(int x)
{   
    int b[2] = {x,x};
    b[12] = -x;
    x--;
    cout<< "x:" << x << " b:" <<b[0] << b[1] << std::endl;
    if (x > 0)
        nested_arrs(x);
    cout<< "x:" << x << " b:" <<b[0] << b[1] << std::endl;
}



void vectorBadDelete()
{
    std::vector<int*>* vec = new std::vector<int*>();
    for(int i = 0;i<5;i++)
    {
        vec->push_back(new int(i));
    }
    for(auto it = (*vec).begin();it!= (*vec).end();it++)
    {
        cout << **it << std::endl;
    }
    (*vec).clear();
    delete vec;
}


void vectorMaybeGoodDelete()
{
    std::vector<int*>* vec = new std::vector<int*>();
    for(int i = 0;i<5;i++)
    {
        vec->push_back(new int(i));
    }
    for(auto it = vec->begin();it!= vec->end();it++)
    {
        cout << **it << std::endl;
    }
    for(auto it = vec->begin();it!= vec->end();it++)
    {
       delete *it;
       vec->erase(it);
    }
    delete vec;
}


void vectorGoodDelete()
{
    std::vector<int*>* vec = new std::vector<int*>();
    for(int i = 0;i<5;i++)
    {
        vec->push_back(new int(i));
    }
    for(auto it = vec->begin();it!= vec->end();it++)
    {
        cout << **it << std::endl;
    }
    for(auto it = vec->begin();it!= vec->end();it++)
    {
       delete *it;
    }
    vec->clear();
    delete vec;
}


void arrayBadDelete()
{
    int** arr = new int*[10];
    for(int i = 0; i < 10; ++i)
        arr[i] = new int(i);
    delete[] arr;
}

void arrayMaybeGoodDelete()
{
    int** arr = new int*[10];
    for(int i = 0; i < 10; ++i)
        arr[i] = new int(i);
    delete[] arr;
    for(int i = 0; i < 10; ++i)
        delete arr[i];
}

void arrayGoodDelete()
{
    int** arr = new int*[10];
    for(int i = 0; i < 10; ++i)
        arr[i] = new int(i);
    for(int i = 0; i < 10; ++i)
        delete arr[i];
    delete[] arr;

}


int main(int argc, char** argv){
    // memoryLeakExample();
    // ilegalHeapAccess();
    // ilegalStackAccess();
    // compileTimeStack();
    // nested_addresses(5);
    // nested_arrs(3);
    // vectorBadDelete();
    // vectorMaybeGoodDelete();
    // vectorGoodDelete();  
    // arrayBadDelete();
    // arrayMaybeGoodDelete();
    // arrayGoodDelete();
    return 0;
}
