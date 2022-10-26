package misc;

    public enum BruteForceDifficulty {
        LEVEL_1 {
            public int value() {
                return 1;
            }
            public String toString() {
                return "Level 1";
            }
        },
        LEVEL_2 {
            public int value() {
                return 2;
            }
            public String toString() {
                return "Level 2";
            }
        },
        LEVEL_3{
            public int value(){
                return 3;
            }
            public String toString(){
                return "Level 3";
            }
        },
        LEVEL_4{
            public int value(){
                return 4;
            }
            public String toString(){
                return "Level 4";
            }
        };


        public static BruteForceDifficulty strToDifficulty(String difficultyFreeText){
            switch(difficultyFreeText){
                case "Easy":
                    return BruteForceDifficulty.LEVEL_1;
                case "Medium":
                    return BruteForceDifficulty.LEVEL_2;
                case "Hard":
                    return BruteForceDifficulty.LEVEL_3;
                case "Insane":
                    return BruteForceDifficulty.LEVEL_4;
                default:
                    throw new RuntimeException("Invalid difficulty given in XML file");
            }
        }

        public abstract int value();
    }

