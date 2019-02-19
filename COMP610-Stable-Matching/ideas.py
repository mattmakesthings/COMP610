

def perm_gen(ll,ul):



    def inc(arr_orig,index):
        arr = arr_orig[:]
        print "  ",arr, " ", index
        if arr == ul:
            return arr

        if arr[index] == ul[index]:
            print "\t",arr, " 1,0 " , index
            arr[index] = ll[index]
            ret=inc(arr,index-1)
            #if ret != -1:
            return ret
            #else:
            #    return arr
        else:
            print "\t",arr, " 2,0 " , index
            arr[index]+=1
            return arr


    permutations = []
    num_it = 1

    #get number of iterations
    for i in range(0,len(ll)):
        num_it*= ul[i]-ll[i]+1
    print num_it
    #call inc for each separate permutation
    permutations.append(ll)
    print permutations
    for i in range(0,num_it):
        permutations.append( inc(permutations[i] , len(permutations[i])-1) )
        print permutations

    return permutations


#inc will increment the last entry in the arr
#then will append it to the end of the arr


def main():
    arr = perm_gen([0,1,0],[2,1,1])
    print arr

if __name__ == "__main__":
    main()
