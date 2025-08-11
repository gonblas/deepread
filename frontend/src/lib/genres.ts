
export const BOOK_GENRES = [
  "ADVENTURE",
  "SCIENCE_FICTION",
  "FANTASY",
  "MYSTERY",
  "HORROR",
  "ROMANCE",
  "DRAMA",
  "COMEDY",
  "HISTORICAL_FICTION",
  "DYSTOPIAN",
  "CRIME",
  "THRILLER",
  "HISTORICAL_ROMANCE",
  "LITRPG",
  "CYBERPUNK",
  "TRUE_CRIME",

  "BIOGRAPHY",
  "ESSAY",
  "HISTORY",
  "POLITICS",
  "ECONOMICS",
  "PHILOSOPHY",
  "PSYCHOLOGY",
  "NATURAL_SCIENCES",
  "SOCIAL_SCIENCES",
  "SELF_HELP",
  "RELIGION",
  "EDUCATION",
  "TECHNOLOGY",
  "HEALTH",
  "BUSINESS",
  "TRAVEL",
  "COOKING",

  "CHILDREN",
  "YOUNG_ADULT",
  "MIDDLE_GRADE",
  "PICTURE_BOOKS",
  "FAIRY_TALES",

  "TEXTBOOK",
  "SCIENTIFIC",
  "PROGRAMMING",
  "MEDICINE",
  "MATHEMATICS",
] as const

export type BookGenre = (typeof BOOK_GENRES)[number]

const SPECIAL_LABELS: Record<string, string> = {
  LITRPG: "LitRPG",
  SCIENCE_FICTION: "Science Fiction",
  HISTORICAL_FICTION: "Historical Fiction",
  HISTORICAL_ROMANCE: "Historical Romance",
  NATURAL_SCIENCES: "Natural Sciences",
  SOCIAL_SCIENCES: "Social Sciences",
  SELF_HELP: "Self Help",
  YOUNG_ADULT: "Young Adult",
  MIDDLE_GRADE: "Middle Grade",
  PICTURE_BOOKS: "Picture Books",
  FAIRY_TALES: "Fairy Tales",
  TRUE_CRIME: "True Crime",
}

const GENRE_COLORS: Record<string, string> = {
  ADVENTURE: "bg-orange-100 text-orange-800 border-orange-200",
  SCIENCE_FICTION: "bg-blue-100 text-blue-800 border-blue-200",
  FANTASY: "bg-purple-100 text-purple-800 border-purple-200",
  MYSTERY: "bg-gray-100 text-gray-800 border-gray-200",
  HORROR: "bg-red-100 text-red-800 border-red-200",
  ROMANCE: "bg-pink-100 text-pink-800 border-pink-200",
  DRAMA: "bg-indigo-100 text-indigo-800 border-indigo-200",
  COMEDY: "bg-yellow-100 text-yellow-800 border-yellow-200",
  HISTORICAL_FICTION: "bg-amber-100 text-amber-800 border-amber-200",
  DYSTOPIAN: "bg-slate-100 text-slate-800 border-slate-200",
  CRIME: "bg-red-100 text-red-800 border-red-200",
  THRILLER: "bg-gray-100 text-gray-800 border-gray-200",
  HISTORICAL_ROMANCE: "bg-rose-100 text-rose-800 border-rose-200",
  LITRPG: "bg-emerald-100 text-emerald-800 border-emerald-200",
  CYBERPUNK: "bg-cyan-100 text-cyan-800 border-cyan-200",
  TRUE_CRIME: "bg-red-100 text-red-800 border-red-200",

  BIOGRAPHY: "bg-teal-100 text-teal-800 border-teal-200",
  ESSAY: "bg-violet-100 text-violet-800 border-violet-200",
  HISTORY: "bg-amber-100 text-amber-800 border-amber-200",
  POLITICS: "bg-blue-100 text-blue-800 border-blue-200",
  ECONOMICS: "bg-green-100 text-green-800 border-green-200",
  PHILOSOPHY: "bg-indigo-100 text-indigo-800 border-indigo-200",
  PSYCHOLOGY: "bg-purple-100 text-purple-800 border-purple-200",
  NATURAL_SCIENCES: "bg-emerald-100 text-emerald-800 border-emerald-200",
  SOCIAL_SCIENCES: "bg-blue-100 text-blue-800 border-blue-200",
  SELF_HELP: "bg-green-100 text-green-800 border-green-200",
  RELIGION: "bg-yellow-100 text-yellow-800 border-yellow-200",
  EDUCATION: "bg-blue-100 text-blue-800 border-blue-200",
  TECHNOLOGY: "bg-gray-100 text-gray-800 border-gray-200",
  HEALTH: "bg-green-100 text-green-800 border-green-200",
  BUSINESS: "bg-emerald-100 text-emerald-800 border-emerald-200",
  TRAVEL: "bg-sky-100 text-sky-800 border-sky-200",
  COOKING: "bg-orange-100 text-orange-800 border-orange-200",

  CHILDREN: "bg-pink-100 text-pink-800 border-pink-200",
  YOUNG_ADULT: "bg-purple-100 text-purple-800 border-purple-200",
  MIDDLE_GRADE: "bg-blue-100 text-blue-800 border-blue-200",
  PICTURE_BOOKS: "bg-yellow-100 text-yellow-800 border-yellow-200",
  FAIRY_TALES: "bg-pink-100 text-pink-800 border-pink-200",

  TEXTBOOK: "bg-gray-100 text-gray-800 border-gray-200",
  SCIENTIFIC: "bg-cyan-100 text-cyan-800 border-cyan-200",
  PROGRAMMING: "bg-slate-100 text-slate-800 border-slate-200",
  MEDICINE: "bg-red-100 text-red-800 border-red-200",
  MATHEMATICS: "bg-blue-100 text-blue-800 border-blue-200",
}

export function getGenreLabel(genre: BookGenre): string {
  if (SPECIAL_LABELS[genre]) {
    return SPECIAL_LABELS[genre]
  }

  return genre
    .split("_")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(" ")
}

export function getGenreColor(genre: BookGenre): string {
  return GENRE_COLORS[genre] || "bg-gray-100 text-gray-800 border-gray-200"
}

export function getAllGenresWithLabels(): Array<{ value: BookGenre; label: string }> {
  return BOOK_GENRES.map((genre) => ({
    value: genre,
    label: getGenreLabel(genre),
  })).sort((a, b) => a.label.localeCompare(b.label))
}
