package nested;

public class swapFix implements Algo {
    // calculated string config
    private String[][] calculations;
    public int excersizeListCount = 10;

    //init function
    public String[][] calc(int setCount, int objPerSet, Integer[] rates, String[] excersizes){
        calculations = new String[setCount][objPerSet];
        randomize(calculations, setCount ,objPerSet, rates, excersizes, 0, 0);
        checker(setCount, objPerSet, 1);
        return calculations;
    }

    //randomize function
    private void randomize (String[][] string, int setCount, int objPerSet, Integer[] rates, String[] excersizes, int stringX, int stringY){
        //randomizes selection
        int randomExcersize = (int) Math.floor(Math.random()*(excersizeListCount));
        // Check if x complete
        if(stringX == (objPerSet-1)){
            //Check if y also complete
            if(stringY == (setCount-1)){
                if (rates[randomExcersize] == 0){
                    randomize(string, setCount, objPerSet, rates, excersizes, stringX, stringY);
                }
                else {
                    string[stringY][stringX] = excersizes[randomExcersize];
                    rates[randomExcersize]--;
                }
                // Done
                return;
            } 
            // Update x and move to next y
            else{
                //Check for complete
                if (rates[randomExcersize] == 0){}
                // Update current pos
                else{
                    string[stringY][stringX] = excersizes[randomExcersize];
                    stringX = 0;
                    stringY++;
                    rates[randomExcersize]--;
                }
                randomize(string, setCount, objPerSet, rates, excersizes, stringX, stringY);
                return;
                // call again and return
            }
        } 
        // neither set's complete        
        else{
            // check for complete
            if (rates[randomExcersize] == 0){}
            // update current pos
            else{
                string[stringY][stringX] = excersizes[randomExcersize];
                //adjusting vars
                stringX++;
                rates[randomExcersize]--;
            }
            //call again and return
            randomize(string, setCount, objPerSet, rates, excersizes, stringX, stringY);
            return;
        }
    }

    private void checker(int setCount, int objPerSet, int phase){
        String holdingPlace = new String("");
        int randomPlacement = (int) Math.floor(Math.random()*(setCount));
        // Set
        for (int h = 0; h < setCount; h++){
            // Object in set
            for (int i = 0; i < objPerSet; i++) {
                // Compared object in set
                for (int j = i+1; j < objPerSet; j++) {
                     if (calculations[h][i].equals(calculations[h][j])) {
                            phase = 0;
                            holdingPlace = calculations[h][i];
                            calculations[h][i] = calculations[randomPlacement][i];
                            calculations[randomPlacement][i] = holdingPlace;
                     }
                }
            }
           
        }
        if (phase == 0){
            checker(setCount, objPerSet, 1);
            return;
        } else {
            return;
        }
    }



}