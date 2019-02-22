package com.theopus.xengine.core;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.theopus.xengine.core.ecs.components.*;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.render.RenderModule;
import com.theopus.xengine.core.render.modules.v2.Ver2Module;
import com.theopus.xengine.core.render.modules.v3.Ver3Module;

import org.joml.Vector3f;

import java.util.List;

import static com.theopus.xengine.core.XEngine.MAIN_CAMERA;

public class EntityFactory {
    private static final ArchetypeBuilder base = new ArchetypeBuilder().add(ModelMatrix.class, Velocity.class, Position.class, Render.class);
    private static final ArchetypeBuilder light = new ArchetypeBuilder().add(Light.class, Position.class);

    @Wire
    private World world;
    @Wire
    private TagManager tagManager;
    @Wire
    private ComponentMapper<Render> rMapper;
    @Wire
    private ComponentMapper<Light> lMapper;
    @Wire
    private ComponentMapper<Position> pMapper;
    @Wire(name = "renderer")
    private BaseRenderer renderer;


    private RenderModule<?> defaultModule;
    private String defaultModel;

    public void setDefaults(Class<? extends  RenderModule<?>> module){
        defaultModule = renderer.get(module);
        defaultModel = defaultModule.models().stream().findFirst().get();
    }

    public int createEntity(){
        return createEntity(new Vector3f(0,0,0));
    }

    public int createEntity(Vector3f position) {
        int entity = world.create(base.build(world));
        defaultModule.bind(defaultModel, entity);
        Render render = rMapper.get(entity);
        render.renderable = true;
        render.model = defaultModel;
        render.renderer = defaultModule.getClass();

        placeAt(entity, position);
        return entity;
    }

    public int createLight(Vector3f color, Vector3f position) {
        int i = world.create(light.build(world));
        Light light = lMapper.get(i);
        light.diffuse.x = color.x;
        light.diffuse.y = color.y;
        light.diffuse.z = color.z;

        placeAt(i, position);
        return i;
    }

    public Position createCamera() {
        int cameraAcnhor = world.create();
        world.getMapper(Position.class).create(cameraAcnhor);
        world.getMapper(Velocity.class).create(cameraAcnhor);
        Archetype cameraArchetype = new ArchetypeBuilder().add(Camera.class).build(world);

        int id = world.create(cameraArchetype);
        Camera camera = world.getMapper(Camera.class).get(id);
        camera.target = cameraAcnhor;
        tagManager.register(MAIN_CAMERA, id);
        return world.getMapper(Position.class).get(cameraAcnhor);
    }


    public void placeAt(int entity, Vector3f position){
        Position p = pMapper.get(entity);
        p.position.x = position.x;
        p.position.y = position.y;
        p.position.z = position.z;
    }
}
