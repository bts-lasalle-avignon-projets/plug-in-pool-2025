package com.example.plug_in_pool;

public class Empoche
{
    private CouleurBille couleur;
    private int          poche;

    public String empocherPremiereBille(int bille)
    {
        CouleurBille couleur = CouleurBille.values()[bille];
        switch (couleur)
        {
            case ROUGE:
                return "R";
            case JAUNE:
                return "J";
            case AUCUNE:
                return "A";
            default:
                return "";
        }
    }
}
