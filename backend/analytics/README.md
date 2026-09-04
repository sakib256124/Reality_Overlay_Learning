# Analytics Pipeline

## Events

The mobile app and cloud functions can submit:

- `object_scanned`
- `learning_time`
- `quiz_completed`
- `search_performed`
- `chat_request`
- `translation_request`
- `recognition_request`
- `api_latency`
- `backend_error`

## Collections

- `analytics`: raw per-user event stream.
- `learningAnalytics`: profile-level aggregates.

## Scaling Notes

Use daily rollup jobs when event volume grows. Keep raw events append-only and query aggregates for dashboards.
