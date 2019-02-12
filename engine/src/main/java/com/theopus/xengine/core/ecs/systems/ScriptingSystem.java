package com.theopus.xengine.core.ecs.systems;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptingSystem {

    public static void main(String[] args) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("print('Hello World!');");

        Bindings bindings = engine.createBindings();
        bindings.put("a", 1);

        Object eval = engine.eval("var b = a + a;", bindings);
        System.out.println(eval);
        Object eval2 = engine.eval("b", bindings);
        System.out.println(eval2);
        Object eval1 = engine.eval("print('Answer is :' + b);", bindings);
        System.out.println(eval1);

    }
}
