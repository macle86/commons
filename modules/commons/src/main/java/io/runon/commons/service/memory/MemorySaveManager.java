package io.runon.commons.service.memory;

/**
 * @author macle
 */
public interface MemorySaveManager<T extends MemorySave> {
    T [] getArray();
    T remove(String id);
}
