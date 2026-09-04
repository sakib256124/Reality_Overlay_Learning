export class HttpError extends Error {
  constructor(
    public readonly statusCode: number,
    message: string,
    public readonly code = "backend_error",
  ) {
    super(message);
  }
}

export function sendError(response: any, error: unknown): void {
  if (error instanceof HttpError) {
    response.status(error.statusCode).json({error: error.message, code: error.code});
    return;
  }

  const message = error instanceof Error ? error.message : "Unknown backend error";
  response.status(500).json({error: message, code: "internal_error"});
}

export function requireMethod(request: any, allowed: string[]): void {
  if (!allowed.includes(request.method)) {
    throw new HttpError(405, "Method not allowed", "method_not_allowed");
  }
}

export function limitString(value: unknown, fallback: string, maxLength: number): string {
  return String(value ?? fallback).trim().slice(0, maxLength);
}

export function sanitizeId(value: unknown, fallback: string, maxLength = 100): string {
  const id = String(value ?? fallback)
    .toLowerCase()
    .replace(/[^a-z0-9_-]+/g, "_")
    .replace(/^_+|_+$/g, "")
    .slice(0, maxLength);
  return id || fallback;
}

export function arrayOfStrings(value: unknown, maxItems: number, maxLength: number): string[] {
  if (!Array.isArray(value)) return [];
  return value.slice(0, maxItems).map((item) => String(item).trim().slice(0, maxLength)).filter(Boolean);
}
