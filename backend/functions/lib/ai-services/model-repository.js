"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getProductionModel = getProductionModel;
exports.registerModel = registerModel;
async function getProductionModel(firebaseAdmin, modelFamily) {
    const snapshot = await firebaseAdmin.firestore()
        .collection("aiModels")
        .where("modelFamily", "==", modelFamily)
        .where("status", "==", "production")
        .orderBy("updatedAt", "desc")
        .limit(1)
        .get();
    const document = snapshot.docs[0];
    if (document) {
        return document.data();
    }
    return {
        modelId: "object-detection-v2",
        modelFamily,
        version: "2.0",
        status: "production",
        accuracy: 0.96,
        storagePath: "ai-models/object-detection/2.0/model.tflite",
        minAppVersion: "1.0.0",
        updatedAt: Date.now(),
    };
}
async function registerModel(firebaseAdmin, descriptor) {
    await firebaseAdmin.firestore()
        .collection("aiModels")
        .doc(descriptor.modelId)
        .set({ ...descriptor, updatedAt: Date.now() }, { merge: true });
}
