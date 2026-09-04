# TensorFlow Lite Model Assets

Place the production object recognition model here:

```text
app/src/main/assets/ml/object_classifier.tflite
app/src/main/assets/ml/labels.txt
```

The current implementation is wired to `ml/object_classifier.tflite`. Until that file exists, the recognition module reports a clear missing-model error instead of crashing.
