package user_types.ally.decryption_manager.decryption_manager_utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class nChoosek implements Supplier<List<Integer>> {
    List<List<Integer>> sets;
    int total;
    int cur;

   public nChoosek(int n, int k){
       this.sets = generate(n, k);
       this.total = sets.size();
       this.cur = 0;
   }

    public List<List<Integer>> generate(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[r];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r - 1] < n) {
            combinations.add(combination.clone());

            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return nChooseKRotors(combinations);
    }

    public List<List<Integer>> nChooseKRotors(List<int[]> rotorsArray){
        List<List<Integer>> lst = new ArrayList<>();
        for(int[] arr : rotorsArray){
            List<Integer> asd = Arrays.stream(arr)
                    .boxed()
                    .collect(Collectors.toList())
                    .stream()
                    .map(i -> i + 1)
                    .collect(Collectors.toList());

            lst.add(asd);
        }
        return lst;
    }

    @Override
    public List<Integer> get() {
       if(cur < total)
           return sets.get(cur++);
       return null;
    }

    public static void main(String[] args) {
       Supplier<List<Integer>> rotorsSupplier = new nChoosek(5, 3);
       Supplier<List<Integer>> rotorPositionSupplier = new Permuter(3);
        List<Integer> cur;
        for(List<Integer> rotors = rotorsSupplier.get(); rotors != null; rotors = rotorsSupplier.get()) {
            System.out.println(rotors);
            for(List<Integer> positions = rotorPositionSupplier.get(); positions != null; positions = rotorPositionSupplier.get()) {
                cur = positions.stream().map(rotors::get).collect(Collectors.toList());
                System.out.println("\t" + cur);
            }

            rotorPositionSupplier = new Permuter(3);
        }
   }
}
