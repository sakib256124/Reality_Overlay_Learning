type FirebaseAdmin = any;

export interface AIModelDescriptor {
  modelId: string;
  modelFamily: string;
  version: string;
  status: "production" | "staging" | "deprecated";
  accuracy: number;
  storagePath: string;
  minAppVersion: string;
  updatedAt: number;
}

export async function getProductionModel(
  firebaseAdmin: FirebaseAdmin,
  modelFamily: string,
): Promise<AIModelDescriptor> {
  const snapshot = await firebaseAdmin.firestore()
    .collection("aiModels")
    .where("modelFamily", "==", modelFamily)
    .where("status", "==", "production")
    .orderBy("updatedAt", "desc")
    .limit(1)
    .get();

  const document = snapshot.docs[0];
  if (document) {
    return document.data() as AIModelDescriptor;
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

export async function registerModel(
  firebaseAdmin: FirebaseAdmin,
  descriptor: AIModelDescriptor,
): Promise<void> {
  await firebaseAdmin.firestore()
    .collection("aiModels")
    .doc(descriptor.modelId)
    .set({...descriptor, updatedAt: Date.now()}, {merge: true});
}
