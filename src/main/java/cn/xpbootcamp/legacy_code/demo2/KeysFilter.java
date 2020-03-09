package cn.xpbootcamp.legacy_code.demo2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeysFilter {
    private ISessionService SessionService;

    public KeysFilter(ISessionService sessionService) {
        SessionService = sessionService;
    }

    public List<String> Filter(List<String> marks, boolean isGoldenKey) {
        List<String> keys = new ArrayList<>();

        if (marks == null || marks.size() == 0) {
            return keys;
        }

        if (isGoldenKey) {
            List<String> goldenKey = SessionService.get("GoldenKey");

            keys.addAll(goldenKey);

            marks = validateGoldenKeys(marks);
        } else {
            List<String> SilverKeys = SessionService.get("SilverKey");
            List<String> CopperKeys = SessionService.get("CopperKey");

            keys.addAll(SilverKeys);
            keys.addAll(CopperKeys);
        }

        return marks.stream().filter(mark -> keys.contains(mark) || IsFakeKey(mark)).collect(Collectors.toList());
    }

    private List<String> validateGoldenKeys(List<String> marks) {
        List<String> golden02Mark = marks.stream().filter(x -> x.startsWith("GD02")).collect(Collectors.toList());
        List<String> invalidKeys = new ArrayList<>();

        for (String mark : golden02Mark) {
            if (marks.stream().noneMatch(x -> x.startsWith("GD01") && mark.substring(4, 6).equals(x.substring(4, 6)))) {
                invalidKeys.add(mark);
            }
        }

        return marks.stream().filter(m -> !invalidKeys.contains(m)).collect(Collectors.toList());
    }

    private boolean IsFakeKey(String mark) {
        return mark.endsWith("FAKE");
    }
}
