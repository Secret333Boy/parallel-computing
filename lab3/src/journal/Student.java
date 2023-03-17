package journal;

import java.util.UUID;

public class Student {
    private final UUID id = UUID.randomUUID();

    private Group group;

    public String getId() {
        return id.toString();
    }
}
