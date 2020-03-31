package sandtechnology.bilibili.response.dynamic.extension;

public class ExtensionHolder {

    private Vote vote;

    public String getVoteInfo() {
        return vote == null ? "" : vote.toString();
    }
}
