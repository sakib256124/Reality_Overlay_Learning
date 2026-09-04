type FirebaseAdmin = any;

export interface StorageAsset {
  objectId: string;
  assetType: "image" | "model3d" | "aiModel" | "resource";
  version: string;
  storagePath: string;
  cdnUrl?: string;
  checksum?: string;
  updatedAt: number;
}

export async function registerStorageAsset(
  firebaseAdmin: FirebaseAdmin,
  asset: StorageAsset,
): Promise<void> {
  await firebaseAdmin.firestore()
    .collection("storageAssets")
    .doc(`${asset.assetType}_${asset.objectId}_${asset.version}`)
    .set({...asset, updatedAt: Date.now()}, {merge: true});
}

export function objectImagePath(objectId: string, version: string, fileName: string): string {
  return `objects/${objectId}/images/${version}/${fileName}`;
}

export function objectModelPath(objectId: string, version: string, fileName: string): string {
  return `objects/${objectId}/models/${version}/${fileName}`;
}

export function aiModelPath(modelFamily: string, version: string, fileName = "model.tflite"): string {
  return `ai-models/${modelFamily}/${version}/${fileName}`;
}
