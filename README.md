# compilit-basic-functions

Minor package which contains several convenience functions for guarding against exceptions.
Say you have a method that could potentially throw an exception, and in that case you would want to
return either null or a default value, then this package would provide you with that functionality.
The API is quite self-explanatory. Here is a little example, however.

### usage

```java


class ExampleClass {

    private final Integer value;

    public byte[] getValueAsAString() {
        return MappingGuards.orNull(() -> value.byteValue()); //returns null when the value is null
    }

}
```


