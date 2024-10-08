import { useContext, useState } from "react";
import {
  Container,
  Box,
  Typography,
  Paper,
  Divider,
  Button,
} from "@mui/material";
import DiscussionItem from "../components/DiscussionItem";
import { useGetDiscussions } from "../hooks/discussion";
import AddIcon from "@mui/icons-material/Add";
import AddDiscussionModal from "../components/AddDiscussionModal.jsx";
import { AppContext } from "../components/AppContext.jsx";

const Discussions = () => {
  const { data: discussions, isLoading, error } = useGetDiscussions();
  const [openModal, setOpenModal] = useState(false);
  const { role } = useContext(AppContext);

  const handleOpenModal = () => setOpenModal(true);
  const handleCloseModal = () => setOpenModal(false);

  return (
    <Container maxWidth="lg">
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 4,
        }}
      >
        <Typography variant="h4" fontWeight="bold">
          Discussions
        </Typography>
        {role === "admin" && (
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={handleOpenModal}
            sx={{
              bgcolor: "#9a0147",
              color: "white",
              "&:hover": { bgcolor: "#7a0138" },
              display: "flex",
              alignItems: "center",
              gap: 1,
            }}
          >
            Add Discussion
          </Button>
        )}
      </Box>
      <Paper
        elevation={2}
        sx={{
          mt: 2,
          p: 2,
          minHeight: "80vh",
          display: "flex",
          flexDirection: "column",
        }}
      >
        <Typography variant="body1" paragraph>
          Welcome to our Discussions section. Here, you can engage in thoughtful
          conversations on various topics, share your insights, and learn from
          others. Each discussion focuses on a specific subject, allowing for
          in-depth exploration and diverse perspectives.
        </Typography>
        <Box sx={{ flexGrow: 1, overflowY: "auto", marginTop: 1 }}>
          {isLoading && <Typography>Loading discussions...</Typography>}
          {error && (
            <Typography color="error">
              Error loading discussions: {error.message}
            </Typography>
          )}
          {discussions && discussions.length > 0 && <Divider />}
          {discussions &&
            discussions.map((discussion) => (
              <DiscussionItem key={discussion.id} discussion={discussion} />
            ))}
        </Box>
      </Paper>
      <AddDiscussionModal open={openModal} onClose={handleCloseModal} />
    </Container>
  );
};

export default Discussions;
