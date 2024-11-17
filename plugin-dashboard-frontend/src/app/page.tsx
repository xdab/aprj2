
export default function Home() {
  return (
    <div className="grid grid-rows-[10px_1fr_10px] items-center justify-items-center min-h-screen p-8 pb-10 gap-8 sm:p-10 font-[family-name:var(--font-geist-sans)]">
      <main className="flex flex-col gap-8 row-start-2 items-center sm:items-start">
        <div className="flex gap-4 items-center flex-col sm:flex-row">
          APRJ Dashboard
        </div>
      </main>
      <footer className="row-start-3 flex gap-4 flex-wrap items-center justify-center">
        <a href="https://github.com/xdab/aprj2">GitHub</a>
      </footer>
    </div>
  );
}
