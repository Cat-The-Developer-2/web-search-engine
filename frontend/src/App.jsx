import { useState, useEffect, useRef } from "react";

const API_BASE = "http://localhost:8080";

export default function App() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const [crawlUrl, setCrawlUrl] = useState("");
  const [crawling, setCrawling] = useState(false);
  const [crawlMsg, setCrawlMsg] = useState("");
  const [showCrawl, setShowCrawl] = useState(false);
  const inputRef = useRef(null);

  useEffect(() => {
    inputRef.current?.focus();
  }, []);

  const search = async (e) => {
    e?.preventDefault();
    if (!query.trim()) return;
    setLoading(true);
    setSearched(true);
    try {
      const res = await fetch(
        `${API_BASE}/search?q=${encodeURIComponent(query)}`,
      );
      const data = await res.json();
      setResults(data);
    } catch {
      setResults([]);
    }
    setLoading(false);
  };

  const crawl = async () => {
    if (!crawlUrl.trim()) return;
    setCrawling(true);
    setCrawlMsg("");
    try {
      await fetch(`${API_BASE}/startCrawl?url=${encodeURIComponent(crawlUrl)}`);
      setCrawlMsg("Crawling started! Check your database.");
    } catch {
      setCrawlMsg("Failed to start crawl.");
    }
    setCrawling(false);
  };

  const getDomain = (url) => {
    try {
      return new URL(url).hostname;
    } catch {
      return url;
    }
  };

  return (
    <div className="min-h-screen bg-[#0a0a0a] text-white font-mono">
      {/* Grid background */}
      <div
        className="fixed inset-0 opacity-[0.03]"
        style={{
          backgroundImage:
            "linear-gradient(#fff 1px, transparent 1px), linear-gradient(90deg, #fff 1px, transparent 1px)",
          backgroundSize: "40px 40px",
        }}
      />

      <div className="relative z-10 max-w-3xl mx-auto px-6 py-16">
        {/* Header */}
        <div
          className={`transition-all duration-500 ${searched ? "mb-8" : "mb-16 mt-20"}`}
        >
          <div className="flex items-center gap-3 mb-2">
            <div className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
            <span className="text-xs text-zinc-500 tracking-[0.3em] uppercase">
              Made with ❤️ by{" "}
              <a
                href="https://github.com/Cat-The-Developer-2"
                target="_blank"
                rel="noopener noreferrer"
                className="text-zinc-400 hover:text-emerald-400 transition-colors border-b border-zinc-700 hover:border-emerald-400 pb-px"
              >
                Aditya Chaurasia
              </a>
            </span>
          </div>
          <h1
            className={`font-bold tracking-tighter transition-all duration-500 ${searched ? "text-2xl" : "text-6xl"}`}
            style={{ fontFamily: "'Courier New', monospace" }}
          >
            <a className="text-white" href="/">
              search
            </a>
            <span className="text-emerald-400">.</span>
          </h1>
          {!searched && (
            <p className="text-zinc-600 text-sm mt-3 tracking-wide">
              crawl the web. find anything.
            </p>
          )}
        </div>

        {/* Search bar */}
        <form onSubmit={search} className="mb-8">
          <div className="flex gap-2">
            <div className="flex-1 relative">
              <span className="absolute left-4 top-1/2 -translate-y-1/2 text-zinc-600 text-sm select-none">
                &gt;
              </span>
              <input
                ref={inputRef}
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="type your query..."
                className="w-full bg-zinc-900 border border-zinc-800 rounded-none pl-9 pr-4 py-3 text-sm text-white placeholder-zinc-600 focus:outline-none focus:border-emerald-400 transition-colors"
              />
            </div>
            <button
              type="submit"
              disabled={loading}
              className="px-6 py-3 bg-emerald-400 text-black text-sm font-bold hover:bg-emerald-300 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? "..." : "RUN"}
            </button>
          </div>
        </form>

        {/* Results */}
        {searched && (
          <div>
            {loading ? (
              <div className="space-y-4">
                {[1, 2, 3].map((i) => (
                  <div
                    key={i}
                    className="border border-zinc-800 p-4 animate-pulse"
                  >
                    <div className="h-3 bg-zinc-800 rounded w-1/4 mb-3" />
                    <div className="h-4 bg-zinc-800 rounded w-3/4 mb-2" />
                    <div className="h-3 bg-zinc-800 rounded w-full" />
                  </div>
                ))}
              </div>
            ) : results.length === 0 ? (
              <div className="border border-zinc-800 p-8 text-center">
                <p className="text-zinc-500 text-sm">
                  no results found for{" "}
                  <span className="text-white">"{query}"</span>
                </p>
                <p className="text-zinc-700 text-xs mt-2">
                  try crawling more pages first
                </p>
              </div>
            ) : (
              <div>
                <p className="text-xs text-zinc-600 mb-4 tracking-wide">
                  {results.length} result{results.length !== 1 ? "s" : ""} for{" "}
                  <span className="text-emerald-400">"{query}"</span>
                </p>
                <div className="space-y-px">
                  {results.map((r, i) => (
                    <a
                      key={r.id || i}
                      href={r.url}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="block border border-zinc-800 p-4 hover:border-emerald-400 hover:bg-zinc-900 transition-all group"
                    >
                      <div className="flex items-center gap-2 mb-2">
                        <span className="text-xs text-zinc-600 group-hover:text-emerald-400 transition-colors">
                          {getDomain(r.url)}
                        </span>
                        <span className="text-zinc-800 text-xs">—</span>
                        <span className="text-xs text-zinc-700 truncate">
                          {r.url}
                        </span>
                      </div>
                      <h3 className="text-sm font-bold text-white mb-1 group-hover:text-emerald-400 transition-colors">
                        {r.title || "Untitled"}
                      </h3>
                      <p className="text-xs text-zinc-500 leading-relaxed line-clamp-2">
                        {r.content || "No content available."}
                      </p>
                    </a>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}

        {/* Crawl section */}
        <div className="mt-12 border-t border-zinc-900 pt-8">
          <button
            onClick={() => setShowCrawl(!showCrawl)}
            className="text-xs text-zinc-600 hover:text-zinc-400 transition-colors tracking-[0.2em] uppercase flex items-center gap-2"
          >
            <span
              className={`transition-transform ${showCrawl ? "rotate-90" : ""}`}
            >
              ▶
            </span>
            Crawl a new site
          </button>

          {showCrawl && (
            <div className="mt-4 flex gap-2">
              <input
                type="text"
                value={crawlUrl}
                onChange={(e) => setCrawlUrl(e.target.value)}
                placeholder="https://example.com"
                className="flex-1 bg-zinc-900 border border-zinc-800 px-4 py-2 text-sm text-white placeholder-zinc-600 focus:outline-none focus:border-zinc-600 transition-colors"
              />
              <button
                onClick={crawl}
                disabled={crawling}
                className="px-5 py-2 border border-zinc-700 text-zinc-400 text-sm hover:border-zinc-500 hover:text-white transition-colors disabled:opacity-50"
              >
                {crawling ? "..." : "CRAWL"}
              </button>
            </div>
          )}
          {crawlMsg && (
            <p className="text-xs text-emerald-400 mt-2">{crawlMsg}</p>
          )}
        </div>

        {/* Footer */}
        <div className="mt-16 flex items-center justify-between text-xs text-zinc-700">
          <span>built in &lt;24hrs</span>
          <span>java + spring boot + mongodb + React CRA + Claude</span>
        </div>
      </div>
    </div>
  );
}
