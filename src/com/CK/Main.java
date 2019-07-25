package com.CK;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        int[] cells = {1,0,0,1,0,0,1,0};
        int N = 1000000000;
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.prisonAfterNDays(cells, N)));
    }
}

class Solution {
    HashMap<int[], int[]> oldToNewCell;

    public int[] prisonAfterNDays(int[] cells, int N) {
        oldToNewCell = new HashMap<>();
        int[] oldCells = cells.clone();
        N = ((N % 14) == 0) ? 14 : (N % 14);
//        N = (N -1) % 14;
        prisonHelper(cells);
        for (int i = 0; i < N; i++) {
            Iterator<int[]> itr = oldToNewCell.keySet().iterator();
            while (itr.hasNext()) {
                int[] key = itr.next();
                if (Arrays.equals(key, oldCells)) {
                    cells = oldToNewCell.get(key).clone();
                }
            }
            oldCells = cells.clone();
        }
        return oldCells;
    }

    private void prisonHelper(int[] cells) {
        if (mapContainsArr(cells)) return;
        int[] oldCells = cells.clone();
        for (int i = 0; i < cells.length; i++) {
            if (i == 0 || i == cells.length - 1) {
                if ((cells[i] & 1) == 0) cells[i] = 0;
                else if ((cells[i] & 1) == 1) cells[i]=1;
                continue;
            }
            if ((cells[i] & 1) == 0) {
                if ((cells[i - 1] & 1) == (cells[i + 1] & 1)) cells[i] = 2;
                else cells[i] = 0;
            } else if ((cells[i] & 1) == 1) {
                if ((cells[i - 1] & 1) == (cells[i + 1] & 1)) cells[i] = 3;
                else cells[i] = 1;
            }
        }
        for (int r = 0; r < cells.length; r++) {
            cells[r] >>= 1;
        }
        int[] newCells = cells.clone();
        oldToNewCell.put(oldCells, newCells);
        prisonHelper(cells);
    }

    private boolean mapContainsArr(int[] oldCells) {
        if (oldToNewCell.isEmpty()) return false;
        Iterator<int[]> itr = oldToNewCell.keySet().iterator();
        while (itr.hasNext()) {
            int[] key = itr.next();
            if (Arrays.equals(key, oldCells)) return true;
        }
        return false;
    }
}

class Solution2 {
    public int[] prisonAfterNDays(int[] cells, int N) {
        Map<Integer, Integer> seen = new HashMap();

        // state  = integer representing state of prison
        int state = 0;
        for (int i = 0; i < 8; ++i) {
            if (cells[i] > 0)
                state ^= 1 << i;
        }

        // While days remaining, simulate a day
        while (N > 0) {
            // If this is a cycle, fast forward by
            // seen.get(state) - N, the period of the cycle.
            if (seen.containsKey(state)) {
                N %= seen.get(state) - N;
            }
            seen.put(state, N);

            if (N >= 1) {
                N--;
                state = nextDay(state);
            }
        }

        // Convert the state back to the required answer.
        int[] ans = new int[8];
        for (int i = 0; i < 8; ++i) {
            if (((state >> i) & 1) > 0) {
                ans[i] = 1;
            }
        }

        return ans;
    }

    public int nextDay(int state) {
        int ans = 0;

        // We only loop from 1 to 6 because 0 and 7 are impossible,
        // as those cells only have one neighbor.
        for (int i = 1; i <= 6; ++i) {
            if (((state >> (i-1)) & 1) == ((state >> (i+1)) & 1)) {
                ans ^= 1 << i;
            }
        }

        return ans;
    }
}