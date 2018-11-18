package com.theopus.client.task;

import com.theopus.client.events.CustomTopics;
import com.theopus.client.events.MoveData;
import com.theopus.xengine.event.EventManager;
import com.theopus.xengine.event.InputData;
import com.theopus.xengine.event.TopicReader;
import com.theopus.xengine.event.TopicWriter;
import com.theopus.xengine.inject.Event;
import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import org.lwjgl.glfw.GLFW;

public class InputMapper extends ComponentTask {

    @Event(topicId = EventManager.Topics.INPUT_DATA, listener = true)
    private TopicReader<InputData> inputTopic;

    @Event(topicId = CustomTopics.MOVE)
    private TopicWriter<MoveData> moveDataTopic;

    @Inject
    public InputMapper() {
        super(Context.WORK, false, 10000);
    }

    @Override
    public void process() throws Exception {
        inputTopic.read().forEach(ide -> {
            InputData data = ide.data();
            if (data.action != GLFW.GLFW_REPEAT) {
                MoveData moveData = new MoveData();
                switch (data.key) {
                    case GLFW.GLFW_KEY_W:
                        moveData.forward(data.action != 0);
                        break;
                    case GLFW.GLFW_KEY_S:
                        moveData.back(data.action != 0);
                        break;
                    case GLFW.GLFW_KEY_A:
                        moveData.left(data.action != 0);
                        break;
                    case GLFW.GLFW_KEY_D:
                        moveData.right(data.action != 0);
                        break;
                    case GLFW.GLFW_KEY_C:
                        moveData.bot(data.action != 0);
                        break;
                    case GLFW.GLFW_KEY_SPACE:
                        moveData.top(data.action != 0);
                        break;
                    case GLFW.GLFW_KEY_F:
                        moveData.rotZ(data.action != 0);
                }
                moveDataTopic.write(new com.theopus.xengine.event.Event<>(moveData));
            }
        });
    }


}
