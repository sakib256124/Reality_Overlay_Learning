# Recommendation Service

Recommendations are generated from:

- Learning profile.
- Weak areas.
- Favorite categories.
- Quiz score trend.
- Search and tutor interactions.

The mobile app keeps a Room-first recommendation cache. Cloud generation can refresh the same Firestore `recommendations` collection.
