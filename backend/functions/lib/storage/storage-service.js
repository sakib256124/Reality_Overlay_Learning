"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.registerStorageAsset = registerStorageAsset;
exports.objectImagePath = objectImagePath;
exports.objectModelPath = objectModelPath;
exports.aiModelPath = aiModelPath;
async function registerStorageAsset(firebaseAdmin, asset) {
    await firebaseAdmin.firestore()
        .collection("storageAssets")
        .doc(`${asset.assetType}_${asset.objectId}_${asset.version}`)
        .set({ ...asset, updatedAt: Date.now() }, { merge: true });
}
function objectImagePath(objectId, version, fileName) {
    return `objects/${objectId}/images/${version}/${fileName}`;
}
function objectModelPath(objectId, version, fileName) {
    return `objects/${objectId}/models/${version}/${fileName}`;
}
function aiModelPath(modelFamily, version, fileName = "model.tflite") {
    return `ai-models/${modelFamily}/${version}/${fileName}`;
}
