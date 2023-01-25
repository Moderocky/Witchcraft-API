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
        PASS, SUCCESS, FAIL
    }
}
