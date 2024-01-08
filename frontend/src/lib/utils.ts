export function lerp(a: number, b: number, alpha: number): number {
  return a + alpha * (b - a);
}
