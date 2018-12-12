package de.fu_berlin.inf.dpp.ui.browser_functions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;

public class TypeRefineryTest {

    class Dummy {
        String label;
        Dummy next;
        int age;

        public Dummy(String label, Dummy next, int age) {
            this.label = label;
            this.next = next;
            this.age = age;
        }
    }

    @Test
    public void stringProcessing() {

        String refinedString = TypeRefinery.refine("Hello World", String.class);

        assertEquals("Hello World", refinedString);
    }

    @Test
    public void jsonDeserialization() {

        Dummy first = new Dummy("Foo", null, 12);
        Dummy second = new Dummy("Bar", first, 20);

        Gson gson = new Gson();
        String json = gson.toJson(second);

        Dummy deserialized = TypeRefinery.refine(json, Dummy.class);

        assertEquals(second.label, deserialized.label);
        assertEquals(second.age, deserialized.age);
        assertEquals(first.label, deserialized.next.label);
        assertEquals(first.age, deserialized.next.age);
    }

    @Test
    public void doubleRefinement() {
        Double browserFunctionInput = new Double(4);

        Integer int4 = TypeRefinery.refine(browserFunctionInput, Integer.class);
        assertEquals(new Integer(4), int4);
        int primInt4 = TypeRefinery.refine(browserFunctionInput, int.class);
        assertEquals(4, primInt4);

        Long long4 = TypeRefinery.refine(browserFunctionInput, Long.class);
        assertEquals(new Long(4), long4);
        long primLong4 = TypeRefinery.refine(browserFunctionInput, long.class);
        assertEquals(4l, primLong4);

        Float float4 = TypeRefinery.refine(browserFunctionInput, Float.class);
        assertEquals(new Float(4), float4);
        float primFloat4 = TypeRefinery.refine(browserFunctionInput,
            float.class);
        assertEquals(4.0f, primFloat4, 0.001);

        Double double4 = TypeRefinery
            .refine(browserFunctionInput, Double.class);
        assertEquals(new Double(4), double4);
        double primDouble4 = TypeRefinery.refine(browserFunctionInput,
            double.class);
        assertEquals(4.0, primDouble4, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalDoubleToString() {

        TypeRefinery.refine(new Double(4), String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalDoubleToBoolean() {

        TypeRefinery.refine(new Double(4), Boolean.class);
    }

    @Test
    public void booleanProcessing() {

        Boolean refinedTrue = TypeRefinery.refine(Boolean.TRUE, Boolean.class);
        Boolean refinedFalse = TypeRefinery
            .refine(Boolean.FALSE, Boolean.class);

        assertEquals(Boolean.TRUE, refinedTrue);
        assertEquals(Boolean.FALSE, refinedFalse);
    }
}
