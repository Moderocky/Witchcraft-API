package mx.kenzie.witchcraft.entity.goal;

public class Interaction {
    
    protected Result result = Result.PASS;
    
    public Result getResult() {
        return result;
    }
    
    public void setResult(Result result) {
        this.result = result;
    }
    
    public enum Result {
        SUCCESS(true),
        CONSUME(true),
        CONSUME_PARTIAL(true),
        PASS(false),
        FAIL(false);
        
        public final boolean run;
        
        Result(boolean run) {
            this.run = run;
        }
    }
}
