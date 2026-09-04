# Object Knowledge Service

Stores educational knowledge in Firestore `objects` and references binary assets in Storage.

## Object Document

```json
{
  "objectId": "apple",
  "name": "Apple",
  "category": "Plant Biology",
  "scientificName": "Malus domestica",
  "description": "A fruit containing fiber, vitamins, and natural sugars.",
  "uses": ["Nutrition", "Botany learning"],
  "facts": ["Apples grow on trees."],
  "imageUrl": "https://...",
  "modelUrl": "https://...",
  "updatedAt": 0
}
```

Use CDN-backed Firebase Storage URLs for images, 3D models, AI model files, and educational resources.
