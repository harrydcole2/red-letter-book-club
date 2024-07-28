import { Container } from "@mui/material";
import BookCarousel from "../components/BookCarousel";

const FeaturedBooks = () => {
  const books = [
    {
      title: "To Kill a Mockingbird",
      author: "Harper Lee",
      genre: "Fiction",
      description: "A classic of modern American literature...",
      coverImage: "url_to_image",
    },
    {
      title: "To Kill a Mockingbird",
      author: "Harper Lee",
      genre: "Fiction",
      description: "A classic of modern American literature...",
      coverImage: "abdul-ahad-sheikh-kUYexCmEPuI-unsplash.jpg",
    },
    {
      title: "1984",
      author: "George Orwell",
      genre: "Dystopian",
      description:
        "1984 by George Orwell is a dystopian novel set in a totalitarian society ruled by the Party...",
      coverImage:
        "https://images.unsplash.com/photo-1537996194471-e657df975ab4?auto=format&fit=crop&w=400&h=250",
    },
    {
      title: "Brave New World",
      author: "Aldous Huxley",
      genre: "Dystopian",
      description:
        "Brave New World explores a future society driven by technology and consumerism...",
      coverImage:
        "https://images.unsplash.com/photo-1537944434965-cf4679d1a598?auto=format&fit=crop&w=400&h=250",
    },
  ]

    const reviews = [
      {
        id: 1,
        value : 5,
        review : "My fav book"
      },
      {
        id: 2,
        value : 2,
        review : "No me gusta"
      },
      {
        id: 3,
        value : 3,
        review : "Mid"
      },
      {
        id: 4,
        value : 4,
        review : "Almost really great"
      },
    ];

  return (
    <Container>
      <h1>Featured Books:</h1>
      <BookCarousel books={books} reviews={reviews} />
    </Container>
  );
};

export default FeaturedBooks;
