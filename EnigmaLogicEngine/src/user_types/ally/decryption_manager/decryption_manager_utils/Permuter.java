package user_types.ally.decryption_manager.decryption_manager_utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Permuter implements Supplier<List<Integer>> {
    private final int[] perms;
    private final int[] indexPerms;
    private final int[] directions;
    private final int[] iSwap;
    private int N; //permute 0..N-1
    private int movingPerm = N;

    static int FORWARD = +1;
    static int BACKWARD = -1;

    public Permuter(int n) {
        this.N = n;
        perms = new int[n];     // permutations
        indexPerms = new int[n];     // index to where each permutation value 0..N-1 is
        directions = new int[n];     // direction = forward(+1) or backward (-1)
        iSwap = new int[n]; //number of swaps we make for each integer at each level
        for (int i = 0; i < n; i++) {
            directions[i] = BACKWARD;
            perms[i] = i;
            indexPerms[i] = i;
            iSwap[i] = i;
        }
        movingPerm = n;
    }

    @Override
    public List<Integer> get() {
        //each call returns the next permutation
        do {
            if (movingPerm == N) {
                movingPerm--;
                return Arrays.stream(perms)
                        .boxed()
                        .collect(Collectors.toList());
            } else if (iSwap[movingPerm] > 0) {
                //swap
                int swapPerm = perms[indexPerms[movingPerm] + directions[movingPerm]];
                perms[indexPerms[movingPerm]] = swapPerm;
                perms[indexPerms[movingPerm] + directions[movingPerm]] = movingPerm;
                indexPerms[swapPerm] = indexPerms[movingPerm];
                indexPerms[movingPerm] = indexPerms[movingPerm] + directions[movingPerm];
                iSwap[movingPerm]--;
                movingPerm = N;
            } else {
                iSwap[movingPerm] = movingPerm;
                directions[movingPerm] = -directions[movingPerm];
                movingPerm--;
            }
        } while (movingPerm > 0);
        return null;
    }
}